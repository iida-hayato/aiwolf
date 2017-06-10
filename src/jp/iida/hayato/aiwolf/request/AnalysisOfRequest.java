package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.strategyplayer.ReceivedRequest;

import java.util.ArrayList;
import java.util.List;


public final class AnalysisOfRequest {

	/** �e�G�[�W�F���g�P�̂ɑ΂���s���v�� */
	private List<List<ReceivedRequest>> requestForSingleAgent = new ArrayList<List<ReceivedRequest>>();

	/** �e�G�[�W�F���g�ɑ΂���s���v���̏W�v���� */
	public ArrayList<Request> TotalRequest;


	public AnalysisOfRequest(int agentNum, List<ReceivedRequest> requests){

		for( int i = 0; i <= agentNum; i++ ){
			requestForSingleAgent.add(new ArrayList<ReceivedRequest>());
		}

		// �G�[�W�F���g���ɏW�v
		TotalRequest = new ArrayList<Request>();
		for( int i = 1; i <= agentNum; i++ ){
			TotalRequest.add( new Request(i) );
		}
		for( ReceivedRequest request : requests ){
			int AgentNo = request.request.agentNo;
			if( AgentNo >= 1 && AgentNo <= agentNum ){
				Request workReq = TotalRequest.get(AgentNo - 1);

				workReq.vote *= Math.pow(request.request.vote, request.weight);
				workReq.inspect *= Math.pow(request.request.inspect, request.weight);
				workReq.guard *= Math.pow(request.request.guard, request.weight);
				workReq.attack *= Math.pow(request.request.attack, request.weight);
			}
		}

	}



	/**
	 * ���[�v�����ő�̃G�[�W�F���g�̏W�v�ύs���v�����擾����
	 * @return
	 */
	public Request getMaxVoteRequest(){

		Request result = null;
		double maxValue = Double.NEGATIVE_INFINITY;

		for( Request req : TotalRequest ){
			if( req.vote > maxValue ){
				result = req;
				maxValue = req.vote;
			}
		}

		return result;

	}


	/**
	 * �肢�v�����ő�̃G�[�W�F���g�̏W�v�ύs���v�����擾����
	 * @return
	 */
	public Request getMaxInspectRequest(){

		Request result = null;
		double maxValue = Double.NEGATIVE_INFINITY;

		for( Request req : TotalRequest ){
			if( req.inspect > maxValue ){
				result = req;
				maxValue = req.inspect;
			}
		}

		return result;

	}


	/**
	 * ��q�v�����ő�̃G�[�W�F���g�̏W�v�ύs���v�����擾����
	 * @return
	 */
	public Request getMaxGuardRequest(){

		Request result = null;
		double maxValue = Double.NEGATIVE_INFINITY;

		for( Request req : TotalRequest ){
			if( req.guard > maxValue ){
				result = req;
				maxValue = req.guard;
			}
		}

		return result;

	}


	/**
	 * �P���v�����ő�̃G�[�W�F���g�̏W�v�ύs���v�����擾����
	 * @return
	 */
	public Request getMaxAttackRequest(){

		Request result = null;
		double maxValue = Double.NEGATIVE_INFINITY;

		for( Request req : TotalRequest ){
			if( req.attack > maxValue ){
				result = req;
				maxValue = req.attack;
			}
		}

		return result;

	}




}
