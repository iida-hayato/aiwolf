package jp.iida.hayato.aiwolf.lib;

import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;


/**
 * ��씻���\���N���X
 */
public final class Judge {

	/** ������o�����҂̃G�[�W�F���gNo */
	public final int agentNo;

	/** ������o���ꂽ�҂̃G�[�W�F���gNo */
	public final int targetAgentNo;

	/** ���茋�� */
	public final Species result;

	/** ������o�������� */
	public final Talk talk;

	/** ����������������� */
	public Talk cancelTalk;


	/**
	 * �R���X�g���N�^
	 * @param agentNo ������o�����҂̃G�[�W�F���gNo
	 * @param targetNo ������o���ꂽ�҂̃G�[�W�F���gNo
	 * @param result ���茋��
	 * @param talk ������o��������
	 */
	public Judge(int agentNo, int targetNo, Species result, Talk talk){
		this.agentNo = agentNo;
		this.targetAgentNo = targetNo;
		this.result = result;
		this.talk = talk;
	}


	/**
	 * ���肪�L���ȏ�Ԃ�
	 * @return
	 */
	public boolean isEnable(){

		if( cancelTalk != null ){
			return false;
		}

		return true;

	}


}
