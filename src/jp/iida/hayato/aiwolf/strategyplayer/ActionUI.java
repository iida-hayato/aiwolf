package jp.iida.hayato.aiwolf.strategyplayer;


/**
 * �s����ݒ肷�邽�߂̋[��UI
 */
public final class ActionUI {

	/** ���[�� */
	public Integer voteAgent;

	/** �P���� */
	public Integer attackAgent;

	/** �肢�� */
	public Integer inspectAgent;

	/** ��q�� */
	public Integer guardAgent;



	/**
	 * �s���ݒ�����Z�b�g����
	 */
	public void reset(){
		voteAgent = null;
		inspectAgent = null;
		attackAgent = null;
		guardAgent = null;
	}


}
