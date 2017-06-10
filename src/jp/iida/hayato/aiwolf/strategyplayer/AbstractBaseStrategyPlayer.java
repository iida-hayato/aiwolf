package jp.iida.hayato.aiwolf.strategyplayer;

import jp.iida.hayato.aiwolf.guess.*;
import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import jp.iida.hayato.aiwolf.learn.MyAgentStatistics;
import jp.iida.hayato.aiwolf.lib.AdvanceGameInfo;
import jp.iida.hayato.aiwolf.lib.AgentParameter;
import jp.iida.hayato.aiwolf.lib.AgentParameterItem;
import jp.iida.hayato.aiwolf.lib.WolfsidePattern;
import jp.iida.hayato.aiwolf.request.*;
import org.aiwolf.client.base.player.AbstractRole;
import org.aiwolf.client.lib.TemplateTalkFactory;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

import java.util.ArrayList;

/**
 * 全ての役職のベースとなるクラス
 */
public abstract class AbstractBaseStrategyPlayer extends AbstractRole {

	/** 拡張ゲーム情報 */
	protected AdvanceGameInfo agi;

	/** 行動を設定するための擬似UI */
	protected ActionUI actionUI = new ActionUI();

	/** 今日投票しようと思っているプレイヤー */
	protected Integer planningVoteAgent;

	/** 自分が最後に宣言した「投票しようと思っているプレイヤー」 */
	protected Integer declaredPlanningVoteAgent;

	/** 自分が最後に行った推理 */
	protected AnalysisOfGuess latestGuess;

	/** 自分が最後に行った行動要求 */
	protected AnalysisOfRequest latestRequest;

	/** 騙る役職 */
	protected Role fakeRole;

	/** 宣言済みの騙る役職 */
	protected Role declaredFakeRole;

	/** 自分が最後に宣言した「襲撃しようと思っているプレイヤー」 */
	protected Integer declaredPlanningAttackAgent;

	/** CO済か */
	protected boolean isCameOut;


	/** 個人の持っている情報 */
	protected AgentParameter agentParam = new AgentParameter();

	/** 保有する推理戦略 */
	protected ArrayList<HasGuessStrategy> guessStrategys = new ArrayList<HasGuessStrategy>();

	/** 保有する行動戦略 */
	protected ArrayList<HasActionStrategy> actionStrategys = new ArrayList<HasActionStrategy>();


	/** エージェント毎の統計情報 */
	protected AgentStatistics agentStatistics = new AgentStatistics();

	/** finish()メソッドを有効にするか（finishが２回来る不具合への対応） */
	protected boolean isEnableFinish;


	// 調整機能

	/** 例外発生時に 例外の再スローを行うか */
	protected final boolean isRethrowException = false;

	/** 途中経過を出力するか */
	protected final boolean isPrintPassageTalk = false;

	/** 終了結果を出力するか */
	protected final boolean isPrintFinishTalk = false;

	/** 学習用のデータを出力するか */
	protected final boolean isPutLearnData = false;



	// デバッグ用

	/** Update()にかかった時間のうち最長のもの（デバッグ用） */
	protected long MaxUpdateTime = Long.MIN_VALUE;

	/** Update()にかかった時間が最長のタイミング（デバッグ用） */
	protected String MaxUpdateTiming;


	/**
	 * コンストラクタ
	 * @param agentStatistics
	 */
	public AbstractBaseStrategyPlayer(AgentStatistics agentStatistics){
		this.agentStatistics = agentStatistics;
	}

	@Override
	public void initialize(GameInfo gameInfo, GameSetting gameSetting) {

		try{
			super.initialize(gameInfo, gameSetting);

			// ゲーム開始時にfinish()を有効にする
			isEnableFinish = true;

			// メンバ初期化
			planningVoteAgent = null;
			declaredPlanningVoteAgent = null;
			declaredFakeRole = null;
			declaredPlanningAttackAgent = null;
			fakeRole = null;
			isCameOut = false;
			guessStrategys = new ArrayList<HasGuessStrategy>();
			actionStrategys = new ArrayList<HasActionStrategy>();
			agentParam = new AgentParameter();


			// 拡張ゲーム情報の初期化
			agi = new AdvanceGameInfo(gameInfo, gameSetting);


			// 個人の持っている情報
			agentParam.setParam(AgentParameterItem.FAVOR_RATE, 0.20, true);
			agentParam.setParam(AgentParameterItem.VOTE_RATE_WTOW, 0.8, true);

			agentParam.setParam(AgentParameterItem.DEVINE_RATE_WTOW_WHITE, 1.05, true);
			agentParam.setParam(AgentParameterItem.DEVINE_RATE_WTOW_BLACK, 0.8, true);


			// 推理戦略を設定
			HasGuessStrategy guessStrategy;

			guessStrategy = new HasGuessStrategy(new FirstImpression(), 1.0);
			guessStrategys.add(guessStrategy);
			guessStrategy = new HasGuessStrategy(new FromGuardRecent(), 1.0);
			guessStrategys.add(guessStrategy);
			guessStrategy = new HasGuessStrategy(new Formation_Basic(), 1.0);
			guessStrategys.add(guessStrategy);
			guessStrategy = new HasGuessStrategy(new COTiming(), 1.0);
			guessStrategys.add(guessStrategy);
			//guessStrategy = new HasGuessStrategy(new AttackObstacle_Guess(), 1.0);
			//guessStrategys.add(guessStrategy);
			guessStrategy = new HasGuessStrategy(new JudgeRecent(), 1.0);
			guessStrategys.add(guessStrategy);

			// 村陣営でのみ用いる推理
			if( gameInfo.getRole().getTeam() == Team.VILLAGER ){
				guessStrategy = new HasGuessStrategy(new Noise(), 1.0);
				guessStrategys.add(guessStrategy);
				guessStrategy = new HasGuessStrategy(new VoteRecent(), 1.0);
				guessStrategys.add(guessStrategy);
				guessStrategy = new HasGuessStrategy(new VoteTarget(), 1.0);
				guessStrategys.add(guessStrategy);
				guessStrategy = new HasGuessStrategy(new FromAgentStatistics_B(agentStatistics), 1.0);
				guessStrategys.add(guessStrategy);
				guessStrategy = new HasGuessStrategy(new Learn_1dVoteSaid(agentStatistics), 1.0);
				guessStrategys.add(guessStrategy);
				guessStrategy = new HasGuessStrategy(new Learn_0dEstimateSaid(agentStatistics), 1.0);
				guessStrategys.add(guessStrategy);
//				guessStrategy = new HasGuessStrategy(new Learn_0dProtectCompany(agentStatistics), 1.0);
//				guessStrategys.add(guessStrategy);
				guessStrategy = new HasGuessStrategy(new Learn_COAndDay(agentStatistics), 1.0);
				guessStrategys.add(guessStrategy);
				guessStrategy = new HasGuessStrategy(new Learn_VoteStack(agentStatistics), 1.0);
				guessStrategys.add(guessStrategy);
				guessStrategy = new HasGuessStrategy(new Learn_1dCompetitionDevine(agentStatistics), 1.0);
				guessStrategys.add(guessStrategy);
			}

			// 狼陣営でのみ用いる推理
			if( gameInfo.getRole().getTeam() == Team.WEREWOLF ){
				guessStrategy = new HasGuessStrategy(new Noise(), 0.5);
				guessStrategys.add(guessStrategy);
				guessStrategy = new HasGuessStrategy(new VoteRecent(), 0.5);
				guessStrategys.add(guessStrategy);
			}

			// 狼でのみ用いる推理
			if( gameInfo.getRole() == Role.WEREWOLF ){
				guessStrategy = new HasGuessStrategy(new Favor(), 1.0);
				guessStrategys.add(guessStrategy);
			}


			// 行動戦略を設定
			HasActionStrategy actStrategy;
			actStrategy = new HasActionStrategy(new FixInfo(), 1.0);
			actionStrategys.add(actStrategy);
			actStrategy = new HasActionStrategy(new FromGuess(), 1.0);
			actionStrategys.add(actStrategy);

			// 村陣営でのみ用いる行動
			if( gameInfo.getRole().getTeam() == Team.VILLAGER ){
				actStrategy = new HasActionStrategy(new RoleWeight(), 1.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new VoteStack(), 1.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new ReticentExecute(), 1.0);
				actionStrategys.add(actStrategy);
//				actStrategy = new HasActionStrategy(new BalanceExecute(), 1.0);
//				actionStrategys.add(actStrategy);
//				actStrategy = new HasActionStrategy(new CheckmateExecute(), 1.0);
//				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new Retaliation(), 1.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new AvoidExecute(), 1.0);
				actionStrategys.add(actStrategy);
			}

			// 占でのみ用いる行動
			if( gameInfo.getRole() == Role.SEER ){
				actStrategy = new HasActionStrategy(new BasicSeer(), 1.0);
				actionStrategys.add(actStrategy);
			}

			// 狩でのみ用いる行動
			if( gameInfo.getRole() == Role.BODYGUARD ){
				actStrategy = new HasActionStrategy(new BasicGuard(), 1.0);
				actionStrategys.add(actStrategy);
			}


			// 狼陣営でのみ用いる行動
			if( gameInfo.getRole().getTeam() == Team.WEREWOLF ){
				actStrategy = new HasActionStrategy(new RoleWeight_Wolfside(), 1.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new BasicAttack(), 1.0);
				actionStrategys.add(actStrategy);
			}

			// 狼でのみ用いる行動
			if( gameInfo.getRole() == Role.WEREWOLF ){
				actStrategy = new HasActionStrategy(new VoteStack(), 3.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new AttackObstacle(), 1.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new EvenControl(), 1.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new AvoidExecute_Werewolf(), 1.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new SeerExecute(), 1.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new PowerPlay_Werewolf(), 1.0);
				actionStrategys.add(actStrategy);
			}

			// 狂人でのみ用いる行動
			if( gameInfo.getRole() == Role.POSSESSED ){
				actStrategy = new HasActionStrategy(new PowerPlay_Possessed(), 1.0);
				actionStrategys.add(actStrategy);
				actStrategy = new HasActionStrategy(new PossessedMove(), 1.0);
				actionStrategys.add(actStrategy);
			}

		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)
			// Do nothing
		}


	}


	@Override
	public void dayStart() {

		try{
			// 行動設定をリセットする
			actionUI.reset();
			planningVoteAgent = null;
			declaredPlanningVoteAgent = null;

			// デバッグ用
			if( false && agi.latestGameInfo.getDay() >= 1 ){
				for( int i=1; i<=15; i++ ){
					double singleScore = latestGuess.getSingleWolfPattern(i).score;
					double teamScore = latestGuess.getMostValidWolfPattern(i).score;
					double voteScore = latestRequest.TotalRequest.get(i-1).vote;
					double inspectScore = latestRequest.TotalRequest.get(i-1).inspect;
					double guardScore = latestRequest.TotalRequest.get(i-1).guard;
					double attackScore = latestRequest.TotalRequest.get(i-1).attack;
					System.out.println( i + "," + singleScore + "," + teamScore  + "," + voteScore  + "," + inspectScore  + "," + guardScore  + "," + attackScore );
				}
			}

		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)
			// Do nothing
		}

	}


	@Override
	public void update(GameInfo gameInfo) {

		try{

			// 時間計測開始
			long starttime = System.currentTimeMillis();

			super.update(gameInfo);

			// 拡張ゲーム情報の更新
			agi.update(gameInfo);


			// 日付更新処理時以外に推理系処理を行う
			if( !agi.isDayUpdate() ){

				// 騙り役職の設定
				setFakeRole();

				// 推理を行う
				execGuess();

				// 行動予約を入れる
				execActionReserve();

			}

			// 時間計測終了
			long endtime = System.currentTimeMillis();
			long updatetime = endtime - starttime;

			// update()の処理時間が最長なら記憶
			if( updatetime > MaxUpdateTime ){
				MaxUpdateTime = updatetime;
				MaxUpdateTiming = new StringBuilder().append(getDay()).append("日目 ").append(gameInfo.getTalkList().size()).append("発言").toString();
			}

		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)
			// Do nothing
		}

	}


	@Override
	public Agent attack() {

		try{
			// 喋らせる
			if( isPrintPassageTalk ){
				putDebugMessage(actionUI.attackAgent.toString() + "を襲撃する");
			}

			if( actionUI.attackAgent == null ){
				return null;
			}

			return Agent.getAgent(actionUI.attackAgent);

		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)
			return agi.latestGameInfo.getAliveAgentList().get(0);

		}

	}


	@Override
	public Agent vote() {

		try{

			if( actionUI.voteAgent == null ){
				// 投票先を宣言出来ていない場合、投票しようと思っていた者に投票
				if( planningVoteAgent == null ){
					return null;
				}
				return Agent.getAgent(planningVoteAgent);
			}
			return Agent.getAgent(actionUI.voteAgent);

		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)
			return agi.latestGameInfo.getAliveAgentList().get(0);

		}

	}


	@Override
	public Agent guard() {

		try{

			if( actionUI.guardAgent == null ){
				return null;
			}
			return Agent.getAgent(actionUI.guardAgent);

		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)
			return agi.latestGameInfo.getAliveAgentList().get(0);

		}

	}


	@Override
	public Agent divine() {

		try{

			if( actionUI.inspectAgent == null ){
				return null;
			}
			return Agent.getAgent(actionUI.inspectAgent);

		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)
			return agi.latestGameInfo.getAliveAgentList().get(0);

		}

	}


	@Override
	public String whisper(){
		return null;
	}


	@Override
	public void finish() {

		try{

			// finish()が無効（処理済み）なら即抜ける
			if( !isEnableFinish ){
				return;
			}

			// finish()を１回処理したら無効にする
			isEnableFinish = false;

			// 終了時の処理を喋らせるか
			if( isPrintFinishTalk ){
				// 喋らせる（おまけ）

				GameInfo gameInfo = agi.latestGameInfo;
				ArrayList<Integer> wolves = new ArrayList<Integer>();
				ArrayList<Integer> possess = new ArrayList<Integer>();
				for( int i = 1; i<= agi.gameSetting.getPlayerNum(); i++ ){
					Role role = gameInfo.getRoleMap().get( Agent.getAgent(i) );
					if( role == Role.WEREWOLF ){
						wolves.add(i);
					}else if( role == Role.POSSESSED ){
						possess.add(i);
					}
				}
				WolfsidePattern dummyWolfside = new WolfsidePattern( wolves ,possess );
				InspectedWolfsidePattern dummyInspect = latestGuess.getPattern(dummyWolfside);
				if( dummyInspect != null ){
					double dummyWolfsideScore = latestGuess.getPattern(dummyWolfside).score;
					putDebugMessage("実際の内訳は " + dummyWolfside.toString() + String.format(" (Score:%.5f) ", dummyWolfsideScore));
				}


				WolfsidePattern mostValidWolfside = latestGuess.getMostValidPattern().pattern;
				double mostValidWolfsideScore = latestGuess.getMostValidPattern().score;
				putDebugMessage("最終日推理は " + mostValidWolfside.toString() + String.format(" (Score:%.5f) ", mostValidWolfsideScore));

				// デバッグメッセージの出力
				putDebugMessage("update() 最長時間は" + MaxUpdateTime + "ms (" + MaxUpdateTiming + ")");
			}

			// 自エージェントの統計の更新
			updateMyStatistics();

			// 統計の更新
			updateStatistics();

		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)
			// Do nothing
		}



	}


	/** 騙り役職を設定する */
	protected void setFakeRole(){
		setFakeRole(null);
	}


	/** 騙り役職を設定する */
	protected void setFakeRole(Role role){
		fakeRole = role;
		agi.fakeRole = role;
	}


	/**
	 * 推理を行う
	 */
	protected void execGuess(){

		GameInfo gameInfo = agi.latestGameInfo;

		GuessManager guessManager = new GuessManager(agi.gameSetting.getPlayerNum());
		ArrayList<Guess> guesses;

		// 推理戦略への引数の設定
		GuessStrategyArgs args = new GuessStrategyArgs();
		args.agi = agi;
		args.agentParam = agentParam;

		// 各推理戦略クラスから推理を取得
		for( HasGuessStrategy hasStrategy : guessStrategys ){
			guesses = hasStrategy.strategy.getGuessList(args);
			guessManager.addGuess(ReceivedGuess.newGuesses(guesses, hasStrategy.strategy));
		}

		// 複数の推理から分析結果を取得する
		AnalysisOfGuess aguess;
		if( !agi.selfViewInfo.wolfsidePatterns.isEmpty() ){
			aguess = new AnalysisOfGuess(agi.gameSetting.getPlayerNum(), agi.selfViewInfo.wolfsidePatterns.values(), guessManager);
		}else{
			// 何らかの理由で内訳破綻した場合、全視点のシステム情報のみを参考にする
			aguess = new AnalysisOfGuess(agi.gameSetting.getPlayerNum(), agi.allViewSystemInfo.wolfsidePatterns.values(), guessManager);
		}

		// 最新の推理として格納する
		latestGuess = aguess;

		// 喋らせる
		if( isPrintPassageTalk ){
			WolfsidePattern mostValidWolfside = aguess.getMostValidPattern().pattern;
			putDebugMessage(mostValidWolfside.toString() + " が怪しい", gameInfo.getDay(), gameInfo.getTalkList().size());
		}

	}


	/**
	 * 行動予約を行う
	 */
	protected void execActionReserve(){

		RequestManager ReqManager = new RequestManager();

		// 行動戦略への引数の設定
		ActionStrategyArgs args = new ActionStrategyArgs();
		args.agi = agi;
		args.view = agi.selfViewInfo;
		args.aguess = latestGuess;
		args.parsonalData = agentParam;

		// 各行動戦略クラスから行動要求を取得
		for( HasActionStrategy hasStrategy : actionStrategys ){
			ArrayList<Request> Requests = hasStrategy.strategy.getRequests(args);
			ReqManager.addRequest(ReceivedRequest.newRequests(Requests, hasStrategy.strategy));
		}

		// 行動要求を集計し、取得する
		AnalysisOfRequest calcRequest = new AnalysisOfRequest(agi.gameSetting.getPlayerNum(), ReqManager.allRequest);


		// 各行動の対象として最も妥当な人物を取得
		int voteAgentNo = calcRequest.getMaxVoteRequest().agentNo;
		int guardAgentNo = calcRequest.getMaxGuardRequest().agentNo;
		int inspectAgentNo = calcRequest.getMaxInspectRequest().agentNo;
		int attackAgentNo = calcRequest.getMaxAttackRequest().agentNo;

		// 投票予定として記憶（実際の投票先セットは投票先を宣言した時に行う）
		planningVoteAgent = voteAgentNo;

		// 投票以外の各行動をセット
		actionUI.guardAgent = guardAgentNo;
		actionUI.inspectAgent = inspectAgentNo;
		actionUI.attackAgent = attackAgentNo;

		// 最新の行動要求として格納する
		latestRequest = calcRequest;

	}


	/**
	 * 回避COが必要かの判断を行う
	 * @return
	 */
	protected boolean isAvoidance(){

		GameInfo gameInfo = agi.latestGameInfo;

		// 投票宣言済みエージェントの数
		int voteAgentCount = 0;

		// エージェント毎の投票予告先を取得する
		Integer[] voteTarget = new Integer[agi.gameSetting.getPlayerNum() + 1];
		for( Agent agent : gameInfo.getAliveAgentList() ){
			voteTarget[agent.getAgentIdx()] = agi.getSaidVoteAgent(agent.getAgentIdx());

			// 投票宣言済みエージェントのカウント
			if( voteTarget[agent.getAgentIdx()] != null ){
				voteAgentCount++;
			}
		}

		// エージェント毎の被投票数を取得する
		int[] voteReceiveNum = new int[agi.gameSetting.getPlayerNum() + 1];
		for( int i = 1; i < voteTarget.length; i++ ){
			if( voteTarget[i] != null ){
				voteReceiveNum[voteTarget[i]]++;
			}
		}

		// 最多票のエージェントの票数を取得する
		int maxVoteCount = 0;
		for( int i = 1; i < voteTarget.length; i++ ){
			if( voteReceiveNum[i] > maxVoteCount ){
				maxVoteCount = voteReceiveNum[i];
			}
		}


		// 吊りが発生しない初日は回避COの必要なし
		if( gameInfo.getDay() < 1 ){
			return false;
		}

		// 3発言目までは回避COしない(それまで自分がOverを返さないようにすること)
		if( agi.getMyTalkNum() < 2 ){
			return false;
		}

		// 投票宣言者が少なければ回避COの必要なし
		if( voteAgentCount < gameInfo.getAliveAgentList().size() * 0.65 ){
			return false;
		}

		// 最多票を得ていれば回避COが必要
		if( voteReceiveNum[gameInfo.getAgent().getAgentIdx()] >= maxVoteCount ){
			return true;
		}

		return false;

	}


	/**
	 * 疑い先を話す文章を取得する(発話履歴の保存も行う)
	 * @return
	 */
	protected String getSuspicionTalkString(){

		// 疑うべき人物を取得する
		Integer suspicionAgentNo = getSuspicionTalkAgentNo();

		// 疑うべき人物がいれば話す
		if( suspicionAgentNo != null ){
			//TODO 記憶せずに自分の発言を追って取得させた方が設計思想としては良い
			// 疑い済として記憶する
			agi.talkedSuspicionAgentList.add(suspicionAgentNo);

			// 発言内容を返す
			String ret = TemplateTalkFactory.estimate( Agent.getAgent(suspicionAgentNo), Role.WEREWOLF );
			return ret;
		}

		// この発言を行わない場合、nullを返す
		return null;

	}


	/**
	 * 信用先を話す文章を取得する(発話履歴の保存も行う)
	 * @return
	 */
	protected String getTrustTalkString(){

		// 最新の分析結果を取得する
		AnalysisOfGuess aguess = latestGuess;

		// 最も妥当な狼陣営のスコアを取得する
		double mostValidWolfsideScore = aguess.getMostValidPattern().score;

		// 生存中の全エージェントを走査
		for( Agent agent : agi.latestGameInfo.getAliveAgentList() ){

			// 自分はスキップ
			if( agent.equals(getMe()) ){
				continue;
			}

			int agentNo = agent.getAgentIdx();

			InspectedWolfsidePattern wolfPattern = aguess.getMostValidWolfPattern(agentNo);
			InspectedWolfsidePattern posPattern = aguess.getMostValidPossessedPattern(agentNo);

			double wolfScore = 0.0;
			double posScore = 0.0;
			if( wolfPattern != null ){
				wolfScore = wolfPattern.score;
			}
			if( posPattern != null ){
				posScore = posPattern.score;
			}

			// 狼・狂人それぞれの最大スコアが小さい
			if( wolfScore < mostValidWolfsideScore * 0.4 &&
					posScore < mostValidWolfsideScore * 0.7 ){

				if( !agi.talkedTrustAgentList.contains(agentNo) ){

					// 信用済として記憶する
					agi.talkedTrustAgentList.add(agentNo);

					Role role;

					// 何かCOしているか
					if( agi.agentState[agentNo].comingOutRole != null ){
						// COした役職と推理する
						role = agi.agentState[agentNo].comingOutRole;
					}else{
						// 村人と推理する
						role = Role.VILLAGER;
					}

					// 発言内容を返す
					String ret = TemplateTalkFactory.estimate(agent, role);
					return ret;
				}
			}

		}

		// この発言を行わない場合、nullを返す
		return null;

	}


	/**
	 * 疑い先として話すべきエージェント番号を取得する
	 * @return エージェント番号(Nullの場合も有)
	 */
	protected Integer getSuspicionTalkAgentNo(){

		// 最新の分析結果を取得する
		AnalysisOfGuess aguess = latestGuess;

		// 最も妥当な狼陣営を取得する
		WolfsidePattern mostValidWolfside = aguess.getMostValidPattern().pattern;

		// 疑い先として発言していない狼を取得する
		ArrayList<Integer> wolves = new ArrayList<Integer>();
		for( Integer wolf : mostValidWolfside.wolfAgentNo ){
			if( !agi.talkedSuspicionAgentList.contains(wolf) ){
				wolves.add(wolf);
			}
		}

		// 対象不在時はNullを返す
		if( wolves.isEmpty() ){
			return null;
		}

		return wolves.get(0);

	}


	/**
	 * 自エージェントの統計の更新
	 */
	protected void updateMyStatistics(){

		GameInfo gameInfo = agi.latestGameInfo;
		WolfsidePattern lastGuessWolfPattern = latestGuess.getMostValidPattern().pattern;

		// ゲーム数+1
		MyAgentStatistics.gameCount += 1;

		// 末路カウント
		switch( agi.agentState[gameInfo.getAgent().getAgentIdx()].causeofDeath ){
			case ALIVE:
				MyAgentStatistics.aliveCount++;
				break;
			case ATTACKED:
				MyAgentStatistics.attackCount++;
				break;
			case EXECUTED:
				MyAgentStatistics.executeCount++;
				break;
			default:
				break;
		}

		// 狼正解数のカウント
		int wolfCorrectCount = 0;
		for( int wolf : lastGuessWolfPattern.wolfAgentNo ){
			if( gameInfo.getRoleMap().get(Agent.getAgent(wolf)) == Role.WEREWOLF ){
				wolfCorrectCount++;
			}
		}
		MyAgentStatistics.wolfCorrectCount[wolfCorrectCount]++;

		// update()の処理時間が最長なら記憶
		if( MaxUpdateTime > MyAgentStatistics.maxUpdateTime ){
			MyAgentStatistics.maxUpdateTime = MaxUpdateTime;
		}

		// デバッグ用文字列の記憶
		MyAgentStatistics.debugStringBuilder.append(wolfCorrectCount).append(",");

	}


	protected void updateStatistics(){

		agentStatistics.addStatictics(agi);


//		for( int agentNo = 1; agentNo <= agi.gameSetting.getPlayerNum(); agentNo++ ){
//			HashMap<String, Double> weightOfGuess = agentStatistics.statistics.get(agentNo).weightOfGuess;
//
//			boolean isWolf = agi.latestGameInfo.getRoleMap().get(Agent.getAgent(agentNo)) == Role.WEREWOLF;
//			boolean isPossessed = agi.latestGameInfo.getRoleMap().get(Agent.getAgent(agentNo)) == Role.POSSESSED;
//
//			for( ReceivedGuess guess : latestGuess.getSingleWolfPattern(agentNo).guesses ){
//
//			}
//
//		}

	}


	/**
	 * デバッグメッセージを喋らせます
	 * @param str
	 */
	protected void putDebugMessage(String str){

		GameInfo gameInfo = agi.latestGameInfo;

		System.out.println("(Agent" + gameInfo.getAgent().getAgentIdx() + ") "
				+ "＞ "
				+ str
				+ "");

	}


	/**
	 * デバッグメッセージを喋らせます
	 * @param str
	 * @param day
	 * @param talkid
	 */
	protected void putDebugMessage(String str, int day, int talkid){

		GameInfo gameInfo = agi.latestGameInfo;

		System.out.println("(Agent" + gameInfo.getAgent().getAgentIdx() + ") "
				+ "＞ "
				+ "(" + day + "日 " + talkid + "発言) "
				+ str
				+ "");

	}


}
