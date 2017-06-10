package jp.iida.hayato.aiwolf.lib;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;

/**
 * �J�~���O�A�E�g(��E�̕\��)��\���N���X
 */
public final class ComingOut {

	/** �G�[�W�F���g�ԍ� */
	public final int agentNo;

	/** CO������E */
	public final Role role;

	/** CO�������� */
	public final Talk commingOutTalk;

	/** CO�P�񂵂����� */
	public Talk cancelTalk;


	/**
	 * �G�[�W�F���g�̎擾
	 * @return
	 */
	public Agent getAgent(){
		return Agent.getAgent(agentNo);
	}

	/**
	 * �R���X�g���N�^
	 * @param agentNo �G�[�W�F���g�ԍ�
	 * @param role CO������E
	 * @param commingOutTalkCO��������
	 */
	public ComingOut(int agentNo, Role role, Talk commingOutTalk){
		this.agentNo = agentNo;
		this.role = role;
		this.commingOutTalk = commingOutTalk;
	}


	/**
	 * CO���L���ȏ�Ԃ�
	 * @return
	 */
	public boolean isEnable(){
		// CO��A����CO�P��O�ɗL��
		if( commingOutTalk != null && cancelTalk == null ){
			return true;
		}

		return false;
	}


	/**
	 * CO���L���ȏ�Ԃ�(�w�肵�������̒��O��ԂƂ��Ĕ���)
	 * @param day ��
	 * @param talkID ����ID
	 * @return
	 */
	public boolean isEnable( int day, int talkID ){

		// CO�ォ
		if( commingOutTalk != null || Common.compareTalkID( commingOutTalk.getDay(), commingOutTalk.getIdx(), day, talkID) == -1 ){
			// �P��O��
			if( cancelTalk == null || Common.compareTalkID( commingOutTalk.getDay(), commingOutTalk.getIdx(), day, talkID) >= 0  ){
				// CO��A����CO�P��O�ɗL��
				return true;
			}
		}

		return false;
	}

}
