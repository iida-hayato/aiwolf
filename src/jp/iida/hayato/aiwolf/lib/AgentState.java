package jp.iida.hayato.aiwolf.lib;

import org.aiwolf.common.data.Role;


/**
 * �G�[�W�F���g�̏�Ԃ�\���N���X
 */
public final class AgentState {


	/** �G�[�W�F���g�ԍ� */
	public final int agentNo;

	/** ���ݗL����CO(��CO����Null) */
	public Role comingOutRole;

	/** ���S���i���̂��������ꂽ���j ��������Null */
	public Integer deathDay;

	/** �����@��������ALIVE */
	public CauseOfDeath causeofDeath;


	/**
	 * �R���X�g���N�^
	 * @param agentno
	 */
	public AgentState(int agentno){
		this.agentNo = agentno;
		this.comingOutRole = null;
		this.deathDay = null;
		this.causeofDeath = CauseOfDeath.ALIVE;
	}




}
