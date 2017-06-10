package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.lib.Common;
import jp.iida.hayato.aiwolf.lib.Judge;
import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * �s����p�u��{�P����p�v
 */
public final class BasicAttack extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;


		// �錾�ςݓ��[��̕��͂��擾
		VoteAnalyzer voteAnalyzer = VoteAnalyzer.loadSaidVote(args.agi);

		// ���Ă���[���ɉ����ďP���𔖂�����
		for( Agent agent : gameInfo.getAliveAgentList() ){
			workReq = new Request(agent);
			workReq.attack = Math.max( 1.00 - voteAnalyzer.getReceiveVoteCount(agent) * 0.08, 0.0 );
			Requests.add(workReq);
		}

		// �ő��[�𓾂Ă���G�[�W�F���g�͏P���悩�珜�O����
		for( Agent agent : voteAnalyzer.getMaxReceiveVoteAgent() ){
			workReq = new Request(agent);
			workReq.attack = 0.05;
			Requests.add(workReq);
		}

		// �e��E��CO�҂��擾
		List<Integer> seers = args.agi.getEnableCOAgentNo(Role.SEER);
		List<Integer> mediums = args.agi.getEnableCOAgentNo(Role.MEDIUM);
		List<Integer> bodyguards = args.agi.getEnableCOAgentNo(Role.BODYGUARD);
		List<Integer> villagers = args.agi.getEnableCOAgentNo(Role.VILLAGER);


		//TODO 100ms���᎞�ԑ���Ȃ��̂Ŗ����ł��B���߂�H
//		// �e�G�[�W�F���g�����񂾏ꍇ���V�~�����[�g
//		if( args.agi.latestGameInfo.getDay() > 0 ){
//
//			for( int i = 1; i < args.agi.gameSetting.getPlayerNum(); i++ ){
//
//				// ���S�ρE�T�͌��X��ΏۂȂ̂ŃX�L�b�v
//				if( args.agi.agentState[i].causeofDeath != CauseOfDeath.ALIVE ||
//				    args.agi.latestGameInfo.getRoleMap().get(Agent.getAgent(i)) == Role.WEREWOLF ){
//					continue;
//				}
//
//				// ���񂾏ꍇ�̎��������_�����肷��
//				ViewpointInfo future;
//				future = new ViewpointInfo(args.agi.selfViewInfo);
//				future.removeWolfPattern(i);
//				if( future.wolfsidePatterns.isEmpty() ){
//					// �������j�]����̂ŏP�����Ȃ�
//					workReq = new Request(i);
//					workReq.attack = 0.05;
//					Requests.add(workReq);
//				}
//
//
//				// ���񂾏ꍇ�̑��l���_�����肷��
//				// ����i�����񂾎��_���쐬�i�S���_�j
//				future = new ViewpointInfo(args.agi.allViewTrustInfo);
//				future.removeWolfPattern(i);
//
//				// �n��̘T��␔(�����̐����Ґ� - ���ݐ�܂ފm���̐�)
//				int grayAndBlackNum = 0;
//				for( int j = 1; j < voteReceiveNum.length; j++ ){
//					// ���ݐ�łȂ� ���� ���� ���� �����̊m�蔒�ł͂Ȃ�
//					if( j != i && args.agi.agentState[j].causeofDeath == CauseOfDeath.ALIVE && !future.isFixWhite(j) ){
//						grayAndBlackNum++;
//					}
//				}
//
//				// �����̎c�菈�Y�����擾
//				int tomorrowRestExucuteNum = Common.getRestExecuteCount(args.agi.latestGameInfo.getAliveAgentList().size() - 2);
//
//				//TODO �݂�悪�O���[���Ƃ��l����(����͔��J�E���g�Ōv�Z)
//				//TODO ���l�̌v�Z�������(����͐����Ōv�Z)
//				//TODO �肢�t�𐶂������ꍇ�͊D�����܂邱�Ƃ��l����
//
//				// �l�݂��i�n��̘T���̐����c��݂萔�ȉ��j
//				if( grayAndBlackNum <= tomorrowRestExucuteNum - 1 ){
//					// �l�ނ̂ŏP�����Ȃ�
//					workReq = new Request(i);
//					workReq.attack = 0.05;
//					Requests.add(workReq);
//				}
//
//			}
//
//		}


		// ���l�������Ă�����P�����Ȃ�
		for( Judge judge : args.agi.getSeerJudgeList() ){
			// �l�Ԃ̐肢�t���Ԉ����������o������
			if( !args.agi.isWolf(judge.agentNo) &&
			    (judge.result == Species.WEREWOLF) != args.agi.isWolf(judge.targetAgentNo) ){
				// �P���v����������
				workReq = new Request(judge.agentNo);
				workReq.attack = 0.5;
				Requests.add(workReq);
			}
		}


		// �肢�t�����ЂłȂ���ΏP�����Ȃ�
		for( Judge judge : args.agi.getSeerJudgeList() ){
			// �����肢
			if( judge.agentNo == judge.targetAgentNo ){
				// �P���v����������
				workReq = new Request(judge.agentNo);
				workReq.attack = 0.5;
				Requests.add(workReq);
			}
		}


		// ����P��
		if( args.agi.latestGameInfo.getDay() == 1 ){

			//TODO ��CO�҂��݂ꂻ���Ȃ�}�~�͂͋C�ɂ��Ȃ��Ă���

			// ��0CO�Ȃ��͏P�����Ȃ�(�D�̐���_��)
			if( seers.isEmpty() ){
				for( int medium : mediums ){
					workReq = new Request( medium );
					workReq.attack = 0.1;
					Requests.add(workReq);
				}
			}

			// ��1CO�Ȃ��͏P�����Ȃ�(��q���\�z����邽��)
			if( seers.size() == 1 ){
				workReq = new Request( seers.get(0) );
				workReq.attack = 0.1;
				Requests.add(workReq);
			}

		}

		// �I�Ղ͐M�p�����_���ő΍R�P�����Ȃ�
		if( args.agi.fakeRole == Role.SEER && args.agi.latestGameInfo.getDay() >= 4 ){
			for( int seer : seers ){
				workReq = new Request( seer );
				workReq.attack = 0.1;
				Requests.add(workReq);
			}
		}

		//TODO ��CO�҂��݂ꂻ���Ȃ�}�~�͂͋C�ɂ��Ȃ��Ă���

		// GJ���o�Ă���ꍇ
		if( args.agi.latestGameInfo.getDay() >= 2 && args.agi.latestGameInfo.getAttackedAgent() == null ){
			// �P����̌��𓾂�
			List<Integer> maxVoteAgentList = Common.getMaxVoteAgentNo( args.agi.latestGameInfo.getAttackVoteList() );

			// ���݂ƒ݂�̔��̉\�����Ȃ�
			if( maxVoteAgentList.indexOf( args.agi.latestGameInfo.getExecutedAgent().getAgentIdx() ) == -1 ){
				// GJ�ʒu�͑����ďP�����Ȃ�
				for( Integer agent : maxVoteAgentList ){
					workReq = new Request( agent );
					workReq.attack = 0.2;
					Requests.add(workReq);
				}
				// ����P�����Ȃ�
				for( int medium : mediums ){
					workReq = new Request( medium );
					workReq.attack = 0.2;
					Requests.add(workReq);
				}
//				// ���̏P���v����������
//				for( int seer : seers ){
//					workReq = new Request( seer );
//					workReq.attack = 0.1;
//					Requests.add(workReq);
//				}
//				for( int medium : mediums ){
//					workReq = new Request( medium );
//					workReq.attack = 0.1;
//					Requests.add(workReq);
//				}
			}
		}

		return Requests;
	}

}
