package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.guess.InspectedWolfsidePattern;
import jp.iida.hayato.aiwolf.lib.CauseOfDeath;
import jp.iida.hayato.aiwolf.lib.Common;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * �s����p�u�l�ݐi�s�v
 */
public final class CheckmateExecute extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;

		// �ꐔ�v�Z
		int executeNum = Common.getRestExecuteCount(gameInfo.getAliveAgentList().size());

		//TODO ���Ґ��Ή�
		// �݂萔���[��or�ŏI���Ȃ�v�Z�͍s��Ȃ�
		if( executeNum > 4 || executeNum <= 1 ){
			return Requests;
		}

		// �l�O�p�^�[���̍ő�X�R�A
		double maxScore = args.aguess.getMostValidPattern().score;

		// PP�����t���O
		HashSet<Integer> PPFlag = new HashSet<Integer>();
		// LW�t���O
		HashSet<Integer> LWFlag = new HashSet<Integer>();

		// �S�l�O�p�^�[���𑖍�
		for( InspectedWolfsidePattern iPattern : args.aguess.getAllPattern().values() ){

			double score = iPattern.score;

			// �X�R�A�������p�^�[���͖�������
			if( score < maxScore * 0.2 ){
				continue;
			}

			// �����l�O���̃J�E���g
			int aliveWolfSideNum = 0;
			int aliveWolfNum = 0;
			for( int wolf : iPattern.pattern.wolfAgentNo ){
				if( args.agi.agentState[wolf].causeofDeath == CauseOfDeath.ALIVE ){
					aliveWolfSideNum++;
					aliveWolfNum++;
				}
			}
			for( int possessed : iPattern.pattern.possessedAgentNo ){
				if( args.agi.agentState[possessed].causeofDeath == CauseOfDeath.ALIVE ){
					aliveWolfSideNum++;
				}
			}

			// �c�T�P��
			if( aliveWolfNum == 1 ){
				for( int wolf : iPattern.pattern.wolfAgentNo ){
					if( args.agi.agentState[wolf].causeofDeath == CauseOfDeath.ALIVE ){
						LWFlag.add(wolf);
					}
				}
			}

			// �݂�~�X��PP�ɂȂ邩�i�݂萔���l�O���ł���ΊY���j
			if( executeNum == aliveWolfSideNum ){
				// �T�w�c�Ɋ܂܂�Ȃ��҂�݂��PP
				for( Agent agent : gameInfo.getAliveAgentList() ){
					int agentNo = agent.getAgentIdx();
					if( !iPattern.pattern.isWolfSide(agentNo) ){
						PPFlag.add(agentNo);
					}
				}
			}

		}

		// LW�̓��󂪑��݁A���݂��Ă�PP���������Ȃ��Ȃ�݂�
		for( int agentNo : LWFlag ){
			if( !PPFlag.contains(PPFlag) ){
				workReq = new Request(agentNo);
				workReq.vote = 1.6;
				Requests.add(workReq);
			}
		}


		return Requests;

	}

}
