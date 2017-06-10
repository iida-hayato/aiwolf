package jp.iida.hayato.aiwolf.lib;

import org.aiwolf.common.data.Talk;


/**
 * ��q������\���N���X
 */
public final class GuardRecent {

	/** ��q�����҂̃G�[�W�F���gNo */
	public final int agentNo;

	/** ��q���ꂽ�҂̃G�[�W�F���gNo */
	public final int targetAgentNo;

	/** ������o�������� */
	public final Talk talk;

	/** ����������������� */
	public Talk cancelTalk;

	/** ���s������(�����q=1) */
	public int execDay;


	/**
	 * �R���X�g���N�^
	 * @param agentNo ������o�����҂̃G�[�W�F���gNo
	 * @param targetNo ������o���ꂽ�҂̃G�[�W�F���gNo
	 * @param talk ������o��������
	 */
	public GuardRecent(int agentNo, int targetNo, Talk talk){
		this.agentNo = agentNo;
		this.targetAgentNo = targetNo;
		this.talk = talk;
	}


	/**
	 * ��q�������L���ȏ�Ԃ�
	 * @return
	 */
	public boolean isEnable(){

		if( cancelTalk != null ){
			return false;
		}

		return true;

	}


}
