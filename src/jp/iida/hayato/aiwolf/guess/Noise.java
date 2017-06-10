package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.condition.OrCondition;
import jp.iida.hayato.aiwolf.condition.RoleCondition;
import jp.iida.hayato.aiwolf.lib.CauseOfDeath;
import jp.iida.hayato.aiwolf.lib.Judge;
import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.net.GameSetting;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * �����u�m�C�Y�v�N���X
 */
public final class Noise extends AbstractGuessStrategy {

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {

		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();

		GameSetting gameSetting = args.agi.gameSetting;


		// �Y������G�[�W�F���g���i�[����ϐ�
		HashSet<Agent> othersCO = new HashSet();		// CO�Ώۂ������ȊO
		HashSet<Agent> enemySideCO = new HashSet();		// PP���������Ȃ��󋵂ł̐l�OCO
		HashSet<Agent> invalidAgent = new HashSet();	// �����ȃG�[�W�F���g�ւ̔���
		HashSet<Agent> invalidAction = new HashSet();	// �����ȍs���錾
		HashSet<Agent> emptyTalk = new HashSet();		// ���Ӗ��Ȕ���
		HashSet<Agent> invalidJudge = new HashSet();	// �����Ȕ���o��


		// �S�Ă̔����������m�F����
		for( int day = 0; day <= args.agi.latestGameInfo.getDay(); day++ ){
			for( Talk talk : args.agi.getTalkList(day) ){

				double correlation = 1.0;

				// �����̏ڍׂ̎擾
				Utterance utterance = args.agi.getUtterance(talk.getContent());

				// ���Ӕ����̏ꍇ�A�����̈Ӗ����擾
				if( utterance.getTopic() == Topic.AGREE ){
					utterance = args.agi.getMeanFromAgreeTalk(talk, 0);
					// ��͕s�\
					if( utterance == null ){
						invalidAction.add(talk.getAgent());
						continue;
					}
				}

				switch( utterance.getTopic() ){
					case COMINGOUT:
						// CO�Ώۂ������ȊO
						if( utterance.getTarget().getAgentIdx() != talk.getAgent().getAgentIdx() ){
							othersCO.add(talk.getAgent());
						}
						// PP���������Ȃ��󋵂ł̐l�OCO
						if( utterance.getRole().getTeam() != Team.VILLAGER ){
							int wolfSideNum = gameSetting.getRoleNum(Role.WEREWOLF) + gameSetting.getRoleNum(Role.POSSESSED);
							if( args.agi.dayInfoList.get(day).aliveAgentList.size() > wolfSideNum * 2 ){
								enemySideCO.add(talk.getAgent());
							}
						}
						break;
					case VOTE:
						// �Ώۂ����݂��Ȃ���
						if( !args.agi.isValidAgentNo( utterance.getTarget().getAgentIdx() ) ){
							invalidAgent.add(talk.getAgent());
							break;
						}
						// �Ώۂ�����
						if( utterance.getTarget().getAgentIdx() == talk.getAgent().getAgentIdx() ){
							invalidAction.add(talk.getAgent());
						}
						// �������_�őΏۂ����S���Ă���
						if( args.agi.getCauseOfDeath( utterance.getTarget().getAgentIdx(), talk.getDay() ) != CauseOfDeath.ALIVE  ){
							invalidAction.add(talk.getAgent());
						}
						break;
					case ESTIMATE:
						// �Ώۂ����݂��Ȃ���
						if( utterance.getTarget().getAgentIdx() < 1 || utterance.getTarget().getAgentIdx() > args.agi.gameSetting.getPlayerNum() ){
							invalidAgent.add(talk.getAgent());
							break;
						}
						// �Ώۂ�����
						if( utterance.getTarget().getAgentIdx() == talk.getAgent().getAgentIdx() ){
							emptyTalk.add(talk.getAgent());
						}
						break;
					case DISAGREE:
						// �����̈Ӗ�����͕s�\
						emptyTalk.add(talk.getAgent());
						break;
					default:
						break;
				}
			}
		}


		// �S�Ă̐蔻����m�F����
		for( Judge judge : args.agi.getSeerJudgeList() ){
			// �s���Ȕ���o��(CO�����ɔ���o���Ȃ�)
			if( judge.talk.equals(judge.cancelTalk) ){
				invalidJudge.add(Agent.getAgent(judge.agentNo));
			}
		}


		// �S�Ă̗씻����m�F����
		for( Judge judge : args.agi.getMediumJudgeList() ){
			// �s���Ȕ���o��(CO�����ɔ���o���Ȃ�)
			if( judge.talk.equals(judge.cancelTalk) ){
				invalidJudge.add(Agent.getAgent(judge.agentNo));
			}
		}



		for( Agent agent : args.agi.latestGameInfo.getAgentList() ){

			double correlation = 1.0;

			// CO�Ώۂ������ȊO
			if( othersCO.contains(agent) ){
				correlation *= 1.50;
			}
			// PP���������Ȃ��󋵂ł̐l�OCO
			if( enemySideCO.contains(agent) ){
				correlation *= 1.50;
			}
			// �����ȃG�[�W�F���g�ւ̔���
			if( invalidAgent.contains(agent) ){
				correlation *= 1.30;
			}
			// �����ȍs���錾
			if( invalidAction.contains(agent) ){
				correlation *= 1.05;
			}
			// ���Ӗ��Ȕ���
			if( emptyTalk.contains(agent) ){
				correlation *= 1.02;
			}
			// �����Ȕ���o��
			if( invalidJudge.contains(agent) ){
				correlation *= 1.60;
			}

			// �W���ɕω����������ꍇ�A������ǉ�����
			if( Double.compare(correlation, 1.0) != 0 ){
				// �Ώۂ��Tor���̃p�^�[����Z������
				RoleCondition wolfCondition = RoleCondition.getRoleCondition( agent.getAgentIdx(), Role.WEREWOLF );
				RoleCondition posCondition = RoleCondition.getRoleCondition( agent.getAgentIdx(), Role.POSSESSED );

				Guess guess = new Guess();
				guess.condition = new OrCondition().addCondition(wolfCondition).addCondition(posCondition);
				guess.correlation = correlation;
				guesses.add(guess);
			}

		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
