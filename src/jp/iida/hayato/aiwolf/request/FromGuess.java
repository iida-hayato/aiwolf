package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.guess.InspectedWolfsidePattern;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * �s����p�u��������̍s���v
 */
public final class FromGuess extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;

		GameInfo gameInfo = args.agi.latestGameInfo;

		// �P�̍l�@�Ɛw�c�l�@�̏d�݂����߂�i���Ղ͔��X���x�ŁA�I�Ղ͂قڐw�c�l�@�݂̂ɂ���j
		double singleScoreWeight = Math.min( 0.1, 0.5 - args.agi.latestGameInfo.getDay() * 0.1 );
		double teamScoreWeight = Math.max( 0.9, 0.5 + args.agi.latestGameInfo.getDay() * 0.1 );


		// �Y���҂��܂܂Ȃ��ő�̃X�R�A
		HashMap<Integer, Double> maxScore = new HashMap<Integer, Double>();

		// �S�l�O�p�^�[���𑖍�
		for( InspectedWolfsidePattern iPattern : args.aguess.getAllPattern().values() ){

			double score = iPattern.score;

			// �T�w�c�Ɋ܂܂�Ȃ��҂ɃX�R�A���L�^
			for( Agent agent : gameInfo.getAliveAgentList() ){
				int agentNo = agent.getAgentIdx();
				if( !iPattern.pattern.isWolfSide(agentNo) ){
					// �Y���҂��܂܂Ȃ��ő�̃X�R�A���v�Z
					if( !maxScore.containsKey(agentNo) || maxScore.get(agentNo) < score ){
						maxScore.put(agentNo, score);
					}
				}
			}

		}

		// �Y���҂̍ő�X�R�A / �Y���҂�����������̍ő�X�R�A ���s���X�R�A�Ɏg�p
		for( Agent agent : gameInfo.getAliveAgentList() ){
			int agentNo = agent.getAgentIdx();

			double score1 = maxScore.getOrDefault(agentNo, 0.0);
			double score2 = args.aguess.getMostValidWolfPattern(agentNo).score;
			double score3 = args.aguess.getMostValidPossessedPattern(agentNo).score;

			// �T�X�R�A�A���X�R�A�̔����̂����傫���ق����g�p
			if( score2 < score3 * 0.5 ){
				score2 = score3 * 0.5;
			}

			workReq = new Request( agentNo );
			if( score1 < 0.0001 ){
				workReq.vote = 2.1;
			}else{
				workReq.vote = Math.min( Math.max(score2 / score1, 0.001), 2.0 );
			}
			workReq.inspect = Math.pow( workReq.vote, 0.4 );
			workReq.guard = 1 / workReq.vote;

			// �s���v���̓o�^
			Requests.add(workReq);
		}

//		// �e�G�[�W�F���g�� �P��/�w�c�ő� �� �T/���l �X�R�A�ɉ����ėv����ς���
//		for( int i = 1; i <= args.agi.gameSetting.getPlayerNum(); i++ ){
//
//			// �P�̘T�p�^�[�����擾
//			InspectedWolfsidePattern singleWolfPattern = args.aguess.getSingleWolfPattern(i);
//			// �T�Ƃ��čł��Ó��ȃp�^�[�����擾
//			InspectedWolfsidePattern mostWolfPattern = args.aguess.getMostValidWolfPattern(i);
//
//			if( mostWolfPattern != null ){
//				// �T�̉\��������
//				workReq = new Request( i );
//				workReq.vote = Math.pow(singleWolfPattern.score, singleScoreWeight) * Math.pow(mostWolfPattern.score, teamScoreWeight) + 0.0001;
//				workReq.inspect = Math.pow( workReq.vote, 0.4 );
//				workReq.guard = 1 / workReq.vote;
//
//				// �s���v���̓o�^
//				Requests.add(workReq);
//
//			}else{
//
//				// �P�̋��l�p�^�[�����擾
//				InspectedWolfsidePattern singlePosPattern = args.aguess.getSinglePossessedPattern(i);
//				// ���l�Ƃ��čł��Ó��ȃp�^�[�����擾
//				InspectedWolfsidePattern mostPosPattern = args.aguess.getMostValidPossessedPattern(i);
//
//				if( mostPosPattern != null ){
//					// �m���T�����A���l�̉\���͂���
//
//					// ���ȊO��
//					if( args.agi.agentState[i].comingOutRole == null ||
//						(args.agi.agentState[i].comingOutRole != Role.SEER && args.agi.agentState[i].comingOutRole != Role.MEDIUM ) ){
//						workReq = new Request( i );
//						workReq.vote = Math.pow(singlePosPattern.score, singleScoreWeight) * Math.pow(mostPosPattern.score, teamScoreWeight) * 0.4 + 0.0001;
//						workReq.guard = 1 / workReq.vote;
//						workReq.inspect = 0.0001;
//					}else{
//						workReq = new Request( i );
//						workReq.vote = Math.pow(singlePosPattern.score, singleScoreWeight) * Math.pow(mostPosPattern.score, teamScoreWeight) * 0.8 + 0.0001;
//						workReq.guard = 1 / workReq.vote;
//						workReq.inspect = 0.0001;
//					}
//
//					// �s���v���̓o�^
//					Requests.add(workReq);
//
//				}else{
//					// �m�葺��
//					workReq = new Request( i );
//					workReq.vote = 0.01;
//					workReq.guard = 1.2;
//					workReq.inspect = 0.0001;
//
//					// �s���v���̓o�^
//					Requests.add(workReq);
//				}
//			}
//		}


		// �������_�Ŋm�荕���ǂ���
		for( int i = 1; i <= args.agi.gameSetting.getPlayerNum(); i++ ){
			if( args.agi.selfViewInfo.isFixBlack(i) ){
				// �������_�Ŋm��T
				workReq = new Request( i );
//				workReq.vote = 1.5;
				workReq.guard = 0.0001;
				workReq.inspect = 0.0001;
			}else if( args.agi.selfViewInfo.isFixWolfSide(i) ){
				// �������_�Ŋm��l�O
				workReq = new Request( i );
//				workReq.vote = 1.2;
				workReq.guard = 0.0001;
				workReq.inspect = 0.0001;
			}
		}


//
//		// �ł��Ó��ȘT�w�c���擾
//		WolfsidePattern pattern = args.aguess.getMostValidPattern().pattern;
//
//
//		// �ł��Ó��ȘT�w�c�̐����T��
//		int aliveWolfNum = 0;
//
//		// �ł��Ó��ȘT�w�c�̘T���
//		for( int wolfAgentNo : pattern.wolfAgentNo ){
//
//			if( args.agi.agentState[wolfAgentNo].causeofDeath == CauseOfDeath.ALIVE ){
//				aliveWolfNum++;
//
//				workReq = new Request( wolfAgentNo );
//				workReq.vote = 1.1;
//				workReq.guard = 1 / workReq.vote;
//
//				// �s���v���̓o�^
//				Requests.add(workReq);
//			}
//
//		}
//
//		// �ł��Ó��ȘT�w�c�̋��l���
//		for( int posAgentNo : pattern.possessedAgentNo ){
//
//			workReq = new Request( posAgentNo );
//			// ��]�T�O�Ȃ狶�l�͕��u�iRPP�˓��ρj
//			if( Common.getRestExecuteCount( gameInfo.getAliveAgentList().size() ) > aliveWolfNum ){
//				workReq.vote = 1.05;
//			}
//			workReq.guard = 1 / workReq.vote;
//
//			// �s���v���̓o�^
//			Requests.add(workReq);
//
//		}




		return Requests;

	}

}
