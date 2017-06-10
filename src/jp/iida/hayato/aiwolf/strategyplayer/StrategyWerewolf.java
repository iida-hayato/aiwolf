package jp.iida.hayato.aiwolf.strategyplayer;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import jp.iida.hayato.aiwolf.lib.*;
import org.aiwolf.client.lib.TemplateTalkFactory;
import org.aiwolf.client.lib.TemplateWhisperFactory;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.net.GameInfo;

import java.util.List;


public class StrategyWerewolf extends AbstractBaseStrategyPlayer {


	/**
	 * コンストラクタ
	 * @param agentStatistics
	 */
	public StrategyWerewolf(AgentStatistics agentStatistics) {
		super(agentStatistics);
	}

	@Override
	public void dayStart() {

		try{
			super.dayStart();

			// 騙り判定の追加
			if( fakeRole == Role.SEER ){
				if( agi.latestGameInfo.getDay() > 0 ){
					addFakeSeerJudge();
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
	public String talk() {

		try{
			String workString;

			// 破綻時の発言
			if( agi.selfViewInfo.wolfsidePatterns.isEmpty() ){

				// とりあえず霊CO
				if( !isCameOut ){
					isCameOut = true;

					String ret = TemplateTalkFactory.comingout( getMe(), Role.MEDIUM );
					return ret;
				}

				return TemplateTalkFactory.over();

			}


			// 騙る役職のＣＯ
			if( fakeRole == Role.SEER ){
				if( !isCameOut ){
					isCameOut = true;

					String ret = TemplateTalkFactory.comingout( getMe(), fakeRole );
					return ret;
				}
			}


			// CO済の場合
			if( isCameOut ){

				// 占CO済
				if( fakeRole == Role.SEER ){

					// 未報告の結果を報告する
					if( agi.reportSelfResultCount < agi.selfInspectList.size() ){

						Judge reportJudge = agi.selfInspectList.get( agi.selfInspectList.size() - 1 );

						// 報告済みの件数を増やす
						agi.reportSelfResultCount++;

						// 発話
						String ret = TemplateTalkFactory
                .divined( Agent.getAgent(reportJudge.targetAgentNo), reportJudge.result );
						return ret;
					}

				}

			}

			// 投票先を言っていない場合、新しい投票先を話す
			if( declaredPlanningVoteAgent == null ){
				// 投票先を変更する
				actionUI.voteAgent = planningVoteAgent;
				declaredPlanningVoteAgent = planningVoteAgent;

				// 最終日は狼の便乗票を防ぐため宣言しない。宣言は最終日以外とする（という名目で村と動きをあわせる）
				if( agi.latestGameInfo.getAliveAgentList().size() > 4 ){
					// 発話
					String ret = TemplateTalkFactory.vote( Agent.getAgent(planningVoteAgent) );
					return ret;
				}
			}

			// 疑い先を狼の人数以上言っていなければ話す
			if( agi.talkedSuspicionAgentList.size() < agi.gameSetting.getRoleNumMap().get(Role.WEREWOLF) ){
				// 疑い先を話す文章を取得し、取得できていれば話す
				workString = getSuspicionTalkString();
				if( workString != null ){
					return workString;
				}
			}

			// 投票先を変更する場合、新しい投票先を話す
			if( declaredPlanningVoteAgent != planningVoteAgent ){
				// 投票先を変更する
				actionUI.voteAgent = planningVoteAgent;
				declaredPlanningVoteAgent = planningVoteAgent;

				// 最終日は狼の便乗票を防ぐため宣言しない。宣言は最終日以外とする（という名目で村と動きをあわせる）
				if( agi.latestGameInfo.getAliveAgentList().size() > 4 ){
					// 発話
					String ret = TemplateTalkFactory.vote( Agent.getAgent(planningVoteAgent) );
					return ret;
				}
			}

			// 疑い先を話す文章を取得し、取得できていれば話す
			workString = getSuspicionTalkString();
			if( workString != null ){
				return workString;
			}

			// 信用先を話す文章を取得し、取得できていれば話す
			workString = getTrustTalkString();
			if( workString != null ){
				return workString;
			}

			// 話す事が無い場合、overを返す
			return TemplateTalkFactory.over();

		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)

			// エラー時はoverを返す
			return TemplateTalkFactory.over();
		}

	}


	/** 騙り役職を設定する */
	@Override
	protected void setFakeRole(){

		// 0日目0発言の最初のwhisper時に設定
		if( fakeRole == null ){

			// 素村騙りで初期化
			setFakeRole(Role.VILLAGER);

			// 確率で占い騙り（暫定）
			if( Math.random() < 0.2 ){
				setFakeRole(Role.SEER);
				// 他に占騙りがいれば素村騙りに戻す
				for( ComingOut co : agi.wisperComingOutList ){
					if( co.isEnable() && co.agentNo != getMe().getAgentIdx() && co.role == Role.SEER ){
						setFakeRole(Role.VILLAGER);
					}
				}
			}

		}

	}


	@Override
	public String whisper(){

		try{

			// 騙る役職の報告
			if( declaredFakeRole != fakeRole ){
				declaredFakeRole = fakeRole;
				return TemplateWhisperFactory.comingout(getMe(), fakeRole);
			}

			// 噛み先の報告
			if( declaredPlanningAttackAgent != actionUI.attackAgent && actionUI.attackAgent != null ){
				declaredPlanningAttackAgent = actionUI.attackAgent;
				return TemplateWhisperFactory.attack( Agent.getAgent(actionUI.attackAgent) );
			}

			return TemplateWhisperFactory.over();
		}catch(Exception ex){
			// 例外を再スローする
			if(isRethrowException){
				throw ex;
			}

			// 以下、例外発生時の代替処理を行う(戻り値があるメソッドの場合は戻す)

			// エラー時はoverを返す
			return TemplateTalkFactory.over();
		}
	}


	@Override
	public Agent attack() {

		try{

			if( actionUI.attackAgent == null ){
				return null;
			}

			//TODO nullにしても勝手に襲撃されるっぽい？要調査
			//		// 生存者５人（処刑後４人）で偶数調整
			//		if( agi.latestGameInfo.getAliveAgentList().size() == 5 ){
			//			return null;
			//		}

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

			// 宣言無視で押し込めば勝てる状態か
			Integer ppVoteAgentNo = getSuspectedPPVoteAgent();
			if( ppVoteAgentNo != null ){
				return Agent.getAgent(ppVoteAgentNo);
			}

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


	/**
	 * 宣言無視で勝てる場合、投票先を取得する
	 * @return
	 */
	public Integer getSuspectedPPVoteAgent(){

		List<Integer> aliveWolfList = agi.getAliveWolfList();

		// あと１人村を吊れば勝てる状態か
		if( aliveWolfList.size() >= Common.getRestExecuteCount(agi.latestGameInfo.getAliveAgentList().size()) ){

			GameInfo gameInfo = agi.latestGameInfo;

			// 奇数進行
			if( agi.latestGameInfo.getAliveAgentList().size() % 2 == 1 ){

				// エージェント毎の投票予告先を取得する
				Integer[] voteTarget = new Integer[agi.gameSetting.getPlayerNum() + 1];
				for( Agent agent : gameInfo.getAliveAgentList() ){
					voteTarget[agent.getAgentIdx()] = agi.getSaidVoteAgent(agent.getAgentIdx());
					if( voteTarget[agent.getAgentIdx()] == null ){
						// 未宣言者がいる場合は不確定要素があるのでやめておく
						return null;
					}
				}

				// エージェント毎の被投票数を取得する
				int[] voteReceiveNum = new int[agi.gameSetting.getPlayerNum() + 1];
				int[] voteReceiveNumWithoutMe = new int[agi.gameSetting.getPlayerNum() + 1];
				for( int i = 1; i < voteTarget.length; i++ ){
					// 投票宣言をカウントする
					if( voteTarget[i] != null ){
						voteReceiveNum[voteTarget[i]]++;
					}
					// 自分以外の投票宣言をカウントする
					if( i != gameInfo.getAgent().getAgentIdx() && voteTarget[i] != null ){
						voteReceiveNumWithoutMe[voteTarget[i]]++;
					}
				}

				// 最多票のエージェントの票数を取得する
				int maxVoteCount = 0;
				for( int i = 1; i < voteTarget.length; i++ ){
					if( voteReceiveNumWithoutMe[i] > maxVoteCount ){
						maxVoteCount = voteReceiveNum[i];
					}
				}

				// 最多票を得ているエージェントを取得
				for( int i = 1; i < voteReceiveNum.length; i++ ){
					if( voteReceiveNumWithoutMe[i] >= maxVoteCount ){
						// 自分の投票を除くと狼が吊られそう？
						if( aliveWolfList.contains(i) ){
							// 他に最多票の人間がいるなら押し込めば勝利
							for( int j = 1; j < voteReceiveNum.length; j++ ){
								if( voteReceiveNumWithoutMe[j] >= maxVoteCount && !aliveWolfList.contains(j) ){
									return j;
								}
							}
							// LW＆他に最多票がいない
							if( aliveWolfList.size() <= 1 ){
								for( int j = 1; j < voteReceiveNum.length; j++ ){
									// 1票差の人間がいれば押し込んでランダム
									if( voteReceiveNumWithoutMe[j] >= maxVoteCount - 1 && !aliveWolfList.contains(j) ){
										return j;
									}
								}
							}
						}
					}
				}

			}

		}

		return null;

	}


	/**
	 * 占い判定を追加する
	 */
	private void addFakeSeerJudge(){

		GameInfo gameInfo = agi.latestGameInfo;

		// 判定先・判定結果の仮設定
		int inspectAgentNo = latestRequest.getMaxInspectRequest().agentNo;
		Species result = Species.HUMAN;

		// 襲撃されたエージェントの取得
		Agent attackedAgent = agi.latestGameInfo.getAttackedAgent();

		// 確率で仲間を占う
		if( Math.random() < 0.2 ){
			for( Agent agent : agi.latestGameInfo.getRoleMap().keySet() ){
				// 仲間狼がグレーで生存しているか
				if( agi.agentState[agent.getAgentIdx()].causeofDeath == CauseOfDeath.ALIVE &&
				    !agi.selfViewInfo.isFixBlack(agent.getAgentIdx()) &&
				    !agi.selfViewInfo.isFixBlack(agent.getAgentIdx()) ){
					inspectAgentNo = agent.getAgentIdx();
					result = Species.HUMAN;
				}
			}
		}

		// 確霊とラインが割れるなら判定を反転する
		List<Integer> mediums = agi.getEnableCOAgentNo(Role.MEDIUM);
		if( mediums.size() == 1 &&  agi.agentState[mediums.get(0)].causeofDeath == CauseOfDeath.ALIVE ){
			if( agi.latestGameInfo.getDay() > 1 && agi.latestGameInfo.getExecutedAgent().getAgentIdx() == inspectAgentNo ){
				// 処刑先を占う場合は霊の判定が後だしになることも考慮
				result = ( agi.isWolf(inspectAgentNo) ) ? Species.WEREWOLF : Species.HUMAN;
			}else{
				if( result == Species.HUMAN ){
					// 白を出した場合の視点を仮定する
					ViewpointInfo future = new ViewpointInfo(agi.selfViewInfo);
					future.removeWolfPattern(inspectAgentNo);
					// 未来視点で霊能が確定人外ならラインが割れるため判定反転
					if( future.isFixWolfSide(mediums.get(0)) ){
						result = Species.WEREWOLF;
					}
				}else{
					// 黒を出した場合の視点を仮定する
					ViewpointInfo future = new ViewpointInfo(agi.selfViewInfo);
					future.removePatternFromJudge(agi.latestGameInfo.getAgent().getAgentIdx(), inspectAgentNo, Species.WEREWOLF);
					// 未来視点で霊能が確定人外ならラインが割れるため判定反転
					if( future.isFixWolfSide(mediums.get(0)) ){
						result = Species.HUMAN;
					}
				}
			}
		}

		// 対象者が自分視点で確定黒の場合、黒出しを行う
		if( agi.selfViewInfo.isFixBlack(inspectAgentNo) ){
			result = Species.WEREWOLF;
		}

		// 占おうとした先が噛まれた
		if( attackedAgent != null && attackedAgent.getAgentIdx() == inspectAgentNo ){
			// 噛み先には人間判定を出す
			result = Species.HUMAN;
		}

		List<Integer> aliveWolfList = agi.getAliveWolfList();


		// あと１人村を吊れば勝てる状態か
		if( aliveWolfList.size() >= Common.getRestExecuteCount(agi.latestGameInfo.getAliveAgentList().size()) ){
			for( Agent agent : agi.latestGameInfo.getAliveAgentList() ){
				// 仲間の狼ではない　かつ　自分視点確白ではない　かつ霊候補ではない　人物か
				if( agi.latestGameInfo.getRoleMap().get(agent) == null &&
				    !agi.selfViewInfo.isFixWhite(agent.getAgentIdx()) &&
				    agi.agentState[agent.getAgentIdx()].comingOutRole != Role.MEDIUM ){
					// 該当者に黒を出す
					inspectAgentNo = agent.getAgentIdx();
					result = Species.WEREWOLF;
					break;
				}
			}
		}



		Judge newJudge = new Judge( getMe().getAgentIdx(), inspectAgentNo, result, null );

		agi.addFakeSeerJudge(newJudge);

	}


}
