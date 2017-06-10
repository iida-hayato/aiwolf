package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.guess.InspectedWolfsidePattern;
import jp.iida.hayato.aiwolf.lib.CauseOfDeath;
import jp.iida.hayato.aiwolf.lib.Common;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * �s����p�u�o�����X�i�s�v
 */
public final class BalanceExecute extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;

		// �ꐔ�v�Z
		int executeNum = Common.getRestExecuteCount(gameInfo.getAliveAgentList().size());

		//TODO ���Ґ��Ή�
		// ����or�ŏI���Ȃ�v�Z�͍s��Ȃ�
		if( gameInfo.getDay() < 3 || executeNum <= 1 ){
			return Requests;
		}

		// �l�O�p�^�[���̍ő�X�R�A
		double maxScore = args.aguess.getMostValidPattern().score;

		// PP�����t���O
		HashMap<Integer, Double> PPMaxScore = new HashMap<Integer, Double>();

		// �S�l�O�p�^�[���𑖍�
		for( InspectedWolfsidePattern iPattern : args.aguess.getAllPattern().values() ){

			double score = iPattern.score;

			// �X�R�A�������p�^�[���͖�������
			if( score < maxScore * 0.2 ){
				continue;
			}

			// �����l�O���̃J�E���g
			int aliveWolfSideNum = 0;
			for( int wolf : iPattern.pattern.wolfAgentNo ){
				if( args.agi.agentState[wolf].causeofDeath == CauseOfDeath.ALIVE ){
					aliveWolfSideNum++;
				}
			}
			for( int possessed : iPattern.pattern.possessedAgentNo ){
				if( args.agi.agentState[possessed].causeofDeath == CauseOfDeath.ALIVE ){
					aliveWolfSideNum++;
				}
			}

			// �݂�~�X��PP�ɂȂ邩�i�݂萔���l�O���ł���ΊY���j
			if( executeNum == aliveWolfSideNum ){
				// �T�w�c�Ɋ܂܂�Ȃ��҂�݂��PP
				for( Agent agent : gameInfo.getAliveAgentList() ){
					int agentNo = agent.getAgentIdx();
					if( !iPattern.pattern.isWolfSide(agentNo) ){
						// PP����������ő�̃X�R�A���擾
						if( !PPMaxScore.containsKey(agentNo) || PPMaxScore.get(agentNo) < score ){
							PPMaxScore.put(agentNo, score);
						}
					}
				}
			}

		}

		// �݂��PP�������������Ȑl���͂Ȃ�ׂ��X���[����
		if( PPMaxScore.size() != gameInfo.getAliveAgentList().size() ){
			for( Map.Entry<Integer, Double> set : PPMaxScore.entrySet() ){
				workReq = new Request(set.getKey());
				workReq.vote = 0.4;
				Requests.add(workReq);
			}
		}


		return Requests;

	}

}
