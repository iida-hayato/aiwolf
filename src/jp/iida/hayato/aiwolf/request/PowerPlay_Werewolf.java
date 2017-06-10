package jp.iida.hayato.aiwolf.request;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;


/**
 * �s����p�uPP(�l�T��)�v
 */
public final class PowerPlay_Werewolf extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;

		GameInfo gameInfo = args.agi.latestGameInfo;


		// PP�˓��ςłȂ���΋󃊃X�g��Ԃ�
		if( !args.agi.isEnablePowerPlay() ){
			return Requests;
		}


		// �l�T�͓��[�Ώۂɂ��Ȃ�
		for( Agent agent : gameInfo.getAgentList() ){
			Role role = gameInfo.getRoleMap().get(agent);
			if( role == Role.WEREWOLF){
				workReq = new Request( agent.getAgentIdx() );
				workReq.vote = 0.0001;
				Requests.add(workReq);
			}
		}

		// ���Ԃ����[���錾�ρA�����[�悪�l�ԂȂ獇�킹��
		for( int agent : args.agi.getAliveWolfList() ){

			Integer target = args.agi.getSaidVoteAgent(agent);

			if( target != null && !args.agi.isWolf(target) ){
				workReq = new Request( target );
				workReq.vote = 1000000.0;

				// �s���v���̓o�^
				Requests.add(workReq);

				return Requests;
			}

		}



		return Requests;

	}

}
