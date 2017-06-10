package jp.iida.hayato.aiwolf.request;

import org.aiwolf.common.data.Agent;

import java.util.List;

/**
 * �s�����N�G�X�g��\���N���X
 */
public final class Request {

	/* �e�s���v���̋��x���A1����Ƃ����{���Ŏw�肷��B */

	public int agentNo;			// �G�[�W�F���g�ԍ�

	public double vote = 1;		// ���[(���Y�Ɠ��`)
	public double attack = 1;	// �P��
	public double guard = 1;	// ��q
	public double inspect = 1;	// �肢


	/**
	 * �R���X�g���N�^
	 * @param agent �G�[�W�F���g
	 */
	public Request(Agent agent){
		this.agentNo = agent.getAgentIdx();
	}


	/**
	 * �R���X�g���N�^
	 * @param agentNo �G�[�W�F���g�ԍ�
	 */
	public Request(int agentNo){
		this.agentNo = agentNo;
	}


	/**
	 * �����̍s���v���̂����A���[�v�����ő�̂��̂��擾����
	 * @param requests
	 * @return
	 */
	public static Request getMaxVoteRequest(List<Request> requests){

		Request result = null;
		double maxValue = Double.NEGATIVE_INFINITY;

		for( Request req : requests ){
			if( req.vote > maxValue ){
				result = req;
				maxValue = req.vote;
			}
		}

		return result;

	}


	/**
	 * �����̍s���v���̂����A�肢�v�����ő�̂��̂��擾����
	 * @param requests
	 * @return
	 */
	public static Request getMaxInspectRequest(List<Request> requests){

		Request result = null;
		double maxValue = Double.NEGATIVE_INFINITY;

		for( Request req : requests ){
			if( req.inspect > maxValue ){
				result = req;
				maxValue = req.inspect;
			}
		}

		return result;

	}


	/**
	 * �����̍s���v���̂����A��q�v�����ő�̂��̂��擾����
	 * @param requests
	 * @return
	 */
	public static Request getMaxGuardRequest(List<Request> requests){

		Request result = null;
		double maxValue = Double.NEGATIVE_INFINITY;

		for( Request req : requests ){
			if( req.guard > maxValue ){
				result = req;
				maxValue = req.guard;
			}
		}

		return result;

	}


	/**
	 * �����̍s���v���̂����A�P���v�����ő�̂��̂��擾����
	 * @param requests
	 * @return
	 */
	public static Request getMaxAttackRequest(List<Request> requests){

		Request result = null;
		double maxValue = Double.NEGATIVE_INFINITY;

		for( Request req : requests ){
			if( req.attack > maxValue ){
				result = req;
				maxValue = req.attack;
			}
		}

		return result;

	}




}
