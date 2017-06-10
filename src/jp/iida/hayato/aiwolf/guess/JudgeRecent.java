package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.condition.AbstractCondition;
import jp.iida.hayato.aiwolf.condition.AndCondition;
import jp.iida.hayato.aiwolf.condition.OrCondition;
import jp.iida.hayato.aiwolf.condition.RoleCondition;
import jp.iida.hayato.aiwolf.lib.AgentParameterItem;
import jp.iida.hayato.aiwolf.lib.CauseOfDeath;
import jp.iida.hayato.aiwolf.lib.Judge;
import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;

import java.util.ArrayList;
import java.util.List;


/**
 * �����u���藚���v�N���X
 */
public final class JudgeRecent extends AbstractGuessStrategy {

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {
		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();

		List<Integer> seers = args.agi.getEnableCOAgentNo(Role.SEER);
		List<Integer> mediums = args.agi.getEnableCOAgentNo(Role.MEDIUM);

		// �S�Ă̐蔻�藚�����m�F����
		for( Judge judge : args.agi.getSeerJudgeList() ){

			AbstractCondition agentWolf = RoleCondition.getRoleCondition( judge.agentNo, Role.WEREWOLF );
			AbstractCondition agentPossessed = RoleCondition.getRoleCondition( judge.agentNo, Role.POSSESSED );
			AbstractCondition targetWolf = RoleCondition.getRoleCondition( judge.targetAgentNo, Role.WEREWOLF );

			// �����Ȕ���ł���΃X�L�b�v
			if( !judge.isEnable() ){
				continue;
			}

			// �l�Ԕ��肩
			if( judge.result == Species.HUMAN){

//				//TODO �������甒�Ⴂ�S�̂̒��Ɉ͂�������m���i�S���p�^�[���̔ے�j�A�Ƃ��������ǂ������H
//				// �T���T�̃p�^�[����Z������i�͂��j
//				Guess guess = new Guess();
//				guess.condition = new AndCondition().addCondition(agentWolf).addCondition(targetWolf);
//				guess.correlation = args.agentParam.getParam(AgentParameterItem.DEVINE_RATE_WTOW_WHITE, 1.0);
//				guesses.add(guess);

			}else{

				Guess guess;

				//TODO ���n��̃`�F�b�N
				// ���o���悪���ȊO��
				if( args.agi.agentState[judge.targetAgentNo].comingOutRole == null ||
					(args.agi.agentState[judge.targetAgentNo].comingOutRole != Role.SEER && args.agi.agentState[judge.targetAgentNo].comingOutRole != Role.MEDIUM ) ){

					// �����T�̃p�^�[���𔖂�����i�딚�j
					guess = new Guess();
					guess.condition = new AndCondition().addCondition(agentPossessed).addCondition(targetWolf);
					guess.correlation = 0.85;
					guesses.add(guess);

					// �T���T�̃p�^�[���𔖂�����i�t�͂��j
					guess = new Guess();
					guess.condition = new AndCondition().addCondition(agentWolf).addCondition(targetWolf);
					guess.correlation = args.agentParam.getParam(AgentParameterItem.DEVINE_RATE_WTOW_BLACK, 1.0);;
					guesses.add(guess);
				}


				//TODO ���̓����΍R���݁E�슚�݂Ȃ猩�Ȃ��悤�ɂ���H
				// ���荕�o���͔�T�Ō���
				if( judge.talk.getDay() == 1 ){
					guess = new Guess();
					guess.condition = agentWolf;
					guess.correlation = 0.95;
					guesses.add(guess);
				}

			}

		}


		// �S�Ă̐�CO�҂��m�F����
		List<Integer> attackedSeers = new ArrayList<Integer>();
		for( int seer : seers ){
			// ���܂ꂽ�肩
			if( args.agi.agentState[seer].causeofDeath == CauseOfDeath.ATTACKED ){
				attackedSeers.add(seer);
			}
		}

		// ���܂ꂽ��̔���͐������ƌ���
		for( Judge judge : args.agi.getSeerJudgeList() ){
			// ���܂ꂽ�肩��̔��肩
			if( attackedSeers.indexOf(judge.agentNo) != -1){

				// �����Ȕ���ł���΃X�L�b�v
				if( !judge.isEnable() ){
					continue;
				}

				// �l�Ԕ��肩
				if( judge.result == Species.HUMAN){
					// �肢�悪�T�̃p�^�[���𔖂�����
					Guess guess = new Guess();
					guess.condition = RoleCondition.getRoleCondition(judge.targetAgentNo, Role.WEREWOLF);
					guess.correlation = 0.80;
					guesses.add(guess);
				}else{
					// �肢�悪�T�̃p�^�[����Z������
					Guess guess = new Guess();
					guess.condition = RoleCondition.getRoleCondition(judge.targetAgentNo, Role.WEREWOLF);
					guess.correlation = 1.20;
					guesses.add(guess);
				}

			}
		}


		// ��̔��萔�`�F�b�N
		for( int seer : seers ){
			if( !isValidSeerJudgeNum( seer, args ) ){
				// �U��Z������
				AbstractCondition agentWolf = RoleCondition.getRoleCondition( seer, Role.WEREWOLF );
				AbstractCondition agentPossessed = RoleCondition.getRoleCondition( seer, Role.POSSESSED );

				Guess guess = new Guess();
				guess.condition = new OrCondition().addCondition(agentWolf).addCondition(agentPossessed);
				guess.correlation = 1.3;
				guesses.add(guess);
			}
		}
		// ��̔��萔�`�F�b�N
		for( int medium : mediums ){
			if( !isValidMediumJudgeNum( medium, args ) ){
				// �U��Z������
				AbstractCondition agentWolf = RoleCondition.getRoleCondition( medium, Role.WEREWOLF );
				AbstractCondition agentPossessed = RoleCondition.getRoleCondition( medium, Role.POSSESSED );

				Guess guess = new Guess();
				guess.condition = new OrCondition().addCondition(agentWolf).addCondition(agentPossessed);
				guess.correlation = 1.3;
				guesses.add(guess);
			}
		}


		// �������X�g��Ԃ�
		return guesses;
	}


	/**
	 * ��̔��萔���Ó���
	 * @param agentNo
	 * @param args
	 * @return
	 */
	private boolean isValidSeerJudgeNum(int agentNo, GuessStrategyArgs args){

		// ���������萔
		int trueJudgeNum;
		if( args.agi.agentState[agentNo].causeofDeath == CauseOfDeath.ALIVE ){
			trueJudgeNum = args.agi.latestGameInfo.getDay();
		}else{
			trueJudgeNum = args.agi.agentState[agentNo].deathDay - 1;
		}

		// ���݂̔��萔�J�E���g
		int judgeCount = 0;
		for( Judge judge : args.agi.getSeerJudgeList() ){
			if( judge.agentNo == agentNo ){
				judgeCount++;
			}
		}

		// ���萔������
		if( judgeCount > trueJudgeNum ){
			return false;
		}

		// ���萔�����Ȃ�
		if( judgeCount < trueJudgeNum ){

			Talk lastTalk = null;
			// �ŐV���̍Ō�̔������擾
			for( Talk talk : args.agi.latestGameInfo.getTalkList() ){
				if( talk.getAgent().getAgentIdx() == agentNo ){
					lastTalk = talk;
				}
			}

			// �ŐV����1��ł��������Ă��邩
			if( lastTalk != null ){
				// ������ �� ���肪���Ȃ��P�[�X��CO����̕񍐒��̂�
				for( int i = args.agi.latestGameInfo.getTalkList().size() - 1; i >= 0 ; i-- ){
					Talk talk = args.agi.latestGameInfo.getTalkList().get(i);
					if( talk.getAgent().getAgentIdx() == agentNo ){

						// ���������
						Utterance utterance = new Utterance(talk.getContent());

						if( utterance.getTopic() == Topic.COMINGOUT ){
							// CO����ŕ񍐒�
							return true;
						}else if( utterance.getTopic() != Topic.DIVINED ){
							// CO�E���ʕ񍐈ȊO�̔������s���Ă���
							return false;
						}

					}
				}

				// �O���܂ł�CO���Ă��茋�ʂ����Ȃ�
				return false;

			}else{
				// ������ �� ���肪�P���Ȃ��̂�����
				if( judgeCount != trueJudgeNum - 1 ){
					return false;
				}
			}

		}

		return true;

	}


	/**
	 * ��̔��萔���Ó���
	 * @param agentNo
	 * @param args
	 * @return
	 */
	private boolean isValidMediumJudgeNum(int agentNo, GuessStrategyArgs args){

		// ���������萔
		int trueJudgeNum;
		if( args.agi.agentState[agentNo].causeofDeath == CauseOfDeath.ALIVE ){
			trueJudgeNum = ( args.agi.latestGameInfo.getDay() < 2 ) ? 0 : (args.agi.latestGameInfo.getDay() - 1);
		}else{
			trueJudgeNum = args.agi.agentState[agentNo].deathDay - 2;
		}


		// ���݂̔��萔�J�E���g
		int judgeCount = 0;
		for( Judge judge : args.agi.getMediumJudgeList() ){
			if( judge.agentNo == agentNo ){
				judgeCount++;
			}
		}

		// ���萔������
		if( judgeCount > trueJudgeNum ){
			return false;
		}

		// ���萔�����Ȃ�
		if( judgeCount < trueJudgeNum ){

			Talk lastTalk = null;
			// �ŐV���̍Ō�̔������擾
			for( Talk talk : args.agi.latestGameInfo.getTalkList() ){
				if( talk.getAgent().getAgentIdx() == agentNo ){
					lastTalk = talk;
				}
			}

			// �ŐV����1��ł��������Ă��邩
			if( lastTalk != null ){
				// ������ �� ���肪���Ȃ��P�[�X��CO����̕񍐒��̂�
				for( int i = args.agi.latestGameInfo.getTalkList().size() - 1; i >= 0 ; i-- ){
					Talk talk = args.agi.latestGameInfo.getTalkList().get(i);
					if( talk.getAgent().getAgentIdx() == agentNo ){

						// ���������
						Utterance utterance = new Utterance(talk.getContent());

						if( utterance.getTopic() == Topic.COMINGOUT ){
							// CO����ŕ񍐒�
							return true;
						}else if( utterance.getTopic() != Topic.INQUESTED ){
							// CO�E���ʕ񍐈ȊO�̔������s���Ă���
							return false;
						}

					}
				}

				// �O���܂ł�CO���Ă��茋�ʂ����Ȃ�
				return false;

			}else{
				// ������ �� ���肪�P���Ȃ��̂�����
				if( judgeCount != trueJudgeNum - 1 ){
					return false;
				}
			}

		}

		return true;

	}

}
