package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.guess.InspectedWolfsidePattern;
import jp.iida.hayato.aiwolf.lib.Judge;
import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * �s����p�u��{���p�v
 */
public final class BasicGuard extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;



		List<Integer> seers = args.agi.getEnableCOAgentNo(Role.SEER);
		List<Integer> mediums = args.agi.getEnableCOAgentNo(Role.MEDIUM);

		// �U�p�^�[���̍ő�X�R�A���ŏ��̂��̂����߂�
		double minScore = Double.MAX_VALUE;
		for( int seer : seers ){
			InspectedWolfsidePattern wolfPattern = args.aguess.getMostValidWolfPattern(seer);
			InspectedWolfsidePattern posPattern = args.aguess.getMostValidPossessedPattern(seer);

			double score = ( wolfPattern != null ? wolfPattern.score : 0.0 ) + ( posPattern != null ? posPattern.score : 0.0 );

			minScore = Math.min(score, minScore);
		}

		// �U�X�R�A�̍����傫������U�ł�����
		int falseCount = 0;
		for( int seer : seers ){
			InspectedWolfsidePattern wolfPattern = args.aguess.getMostValidWolfPattern(seer);
			InspectedWolfsidePattern posPattern = args.aguess.getMostValidPossessedPattern(seer);
			double score = ( wolfPattern != null ? wolfPattern.score : 0.0 ) + ( posPattern != null ? posPattern.score : 0.0 );

			if( score > minScore * 1.6 ){
				falseCount++;
			}
		}

		// �P�l�����ċU�ł���
		if( falseCount == seers.size() - 1 ){
			// �^�ł�������̌�q����������
			for( int seer : seers ){
				InspectedWolfsidePattern wolfPattern = args.aguess.getMostValidWolfPattern(seer);
				InspectedWolfsidePattern posPattern = args.aguess.getMostValidPossessedPattern(seer);

				double score = ( wolfPattern != null ? wolfPattern.score : 0.0 ) + ( posPattern != null ? posPattern.score : 0.0 );

				if( Double.compare(score, minScore) == 0 ){
					workReq = new Request(seer);
					workReq.guard = 3.0;
					Requests.add(workReq);
				}else{
					workReq = new Request(seer);
					workReq.guard = 0.5;
					Requests.add(workReq);
				}
			}
		}


		//TODO ���Ґ��Ή��E�����@�œ���or�s�݂����������p�^�[���̑Ή�(�e�莋�_������΁A�����D�̑S���ɐF�����Ă��邩�Ŕ��f�\)
		// �d���I��������͌�q���Ȃ�
		for( int seer : seers ){
			// ��E��E����ȊO�̐F�����������l�O�����J�E���g
			int seerEnemyCnt = seers.size() - 1;
			int mediumEnemyCnt = ( mediums.size() > 1 ) ? (mediums.size() - 1) : 0;
			int hitGrayBlackCnt = 0;
			for( Judge judge : args.agi.getSeerJudgeList() ){
				if( judge.isEnable() &&
				    judge.agentNo == seer &&
				    judge.result == Species.WEREWOLF ){
					// ���肪���ȊO��
					if( args.agi.agentState[judge.targetAgentNo].comingOutRole == null ||
						(args.agi.agentState[judge.targetAgentNo].comingOutRole != Role.SEER && args.agi.agentState[judge.targetAgentNo].comingOutRole != Role.MEDIUM ) ){
						hitGrayBlackCnt++;
					}
				}
			}

			if( seerEnemyCnt + mediumEnemyCnt + hitGrayBlackCnt >= 4 ){
				workReq = new Request(seer);
				workReq.guard = 0.001;
				Requests.add(workReq);
			}
		}


		// �肢�t���L�p�łȂ���Ό�q���Ȃ�
		for( Judge judge : args.agi.getSeerJudgeList() ){
			// �����肢
			if( judge.agentNo == judge.targetAgentNo ){
				// ��q�v����������
				workReq = new Request(judge.agentNo);
				workReq.guard = 0.5;
				Requests.add(workReq);
			}
		}


		// �錾�ςݓ��[��̕��͂��擾
		VoteAnalyzer voteAnalyzer = VoteAnalyzer.loadSaidVote(args.agi);

		// ���Ă���[���ɉ����Č�q�𔖂�����
		for( Agent agent : gameInfo.getAliveAgentList() ){
			workReq = new Request(agent);
			workReq.guard = Math.max( 1.00 - voteAnalyzer.getReceiveVoteCount(agent) * 0.03, 0.0 );
			Requests.add(workReq);
		}

		// �ő��[�𓾂Ă���G�[�W�F���g�͌�q�悩�珜�O����
		for( Agent agent : voteAnalyzer.getMaxReceiveVoteAgent() ){
			workReq = new Request(agent);
			workReq.guard = 0.01;
			Requests.add(workReq);
		}

		return Requests;
	}

}
