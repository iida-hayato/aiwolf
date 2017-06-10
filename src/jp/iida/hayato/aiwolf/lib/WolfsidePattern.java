package jp.iida.hayato.aiwolf.lib;

import java.util.ArrayList;


/**
 * �T�w�c��\������N���X
 */
public final class WolfsidePattern{

	/** �T�w�c�łȂ����Ƃ�\���R�[�h */
	static final char NOTWOLFSIDE_CODE = '0';

	/** �T�ł��邱�Ƃ�\���R�[�h */
	static final char WOLF_CODE = '1';

	/** ���l�ł��邱�Ƃ�\���R�[�h */
	static final char POSSESSED_CODE = '2';


	/** �l�T�̃G�[�W�F���g�ԍ�(�����Őݒ肷�邱��) */
	public final ArrayList<Integer> wolfAgentNo;

	/** ���l�̃G�[�W�F���g�ԍ�(�����Őݒ肷�邱��) */
	public final ArrayList<Integer> possessedAgentNo;

	/** �T�w�c�̖��� */
	public String wolfSideName;


	/**
	 * �R���X�g���N�^
	 * @param wolfAgentNo �l�T�̃G�[�W�F���g�ԍ�(�����Őݒ肷�邱��)
	 * @param possessedAgentNo ���l�̃G�[�W�F���g�ԍ�(�����Őݒ肷�邱��)
	 */
	public WolfsidePattern(ArrayList<Integer> wolfAgentNo, ArrayList<Integer> possessedAgentNo){
		this.wolfAgentNo = wolfAgentNo;
		this.possessedAgentNo = possessedAgentNo;

		// �T�w�c�̖��̂̐ݒ�
		setWolfSideName();
	}


	/**
	 * ����̃G�[�W�F���g���T��
	 * @param agentno
	 * @return
	 */
	public boolean isWolf(int agentno){

		// �����ꂩ�̘T�ԍ��ƈ�v���邩
		return wolfAgentNo.contains(agentno);

	}


	/**
	 * ����̃G�[�W�F���g�����l��
	 * @param agentno
	 * @return
	 */
	public boolean isPossessed(int agentno){

		// �����ꂩ�̋��l�ԍ��ƈ�v���邩
		return possessedAgentNo.contains(agentno);

	}


	/**
	 * ����̃G�[�W�F���g���T�w�c��
	 * @param agentno
	 * @return
	 */
	public boolean isWolfSide(int agentno){

		// �����ꂩ�̘T�ԍ��ƈ�v���邩�A�܂��͂����ꂩ�̋��l�ԍ��ƈ�v���邩
		return ( wolfAgentNo.contains(agentno) || possessedAgentNo.contains(agentno) );

	}


	/**
	 * �T�w�c�̃R�[�h��Ԃ�
	 * �G�[�W�F���g�P���Q�����ځA�G�[�W�F���g�Q���R�����ڂƂ��āA�T��"1"�A���l��"2"�A�ǂ���ł��Ȃ��҂�"0"�ŕ\��
	 * �T[1,4,5]��[3]�@���@"01021100000000000"
	 * charAt(agentNo)�ŊY���G�[�W�F���g�̏����擾�ł���
	 * @return
	 */
	public String getWolfSideCode(){

		StringBuilder sb = new StringBuilder();

		//TODO �l�����Ȃ�Ƃ�
		for( int i=0; i<20; i++ ){
			sb.append(NOTWOLFSIDE_CODE);
		}

		for( int wolf : wolfAgentNo ){
			sb.setCharAt(wolf, WOLF_CODE);
		}
		for( int pos : possessedAgentNo ){
			sb.setCharAt(pos, POSSESSED_CODE);
		}

		return sb.toString();

	}


	/**
	 * �T�w�c�̖��̂̐ݒ�
	 */
	private void setWolfSideName(){

		StringBuilder sb = new StringBuilder();

		// �T���ꗗ�\���@��j�T[1,2,3]
		sb.append("�T[");
		for( int wolf : wolfAgentNo ){
			sb.append(wolf).append(",");
		}
		if( !wolfAgentNo.isEmpty() ){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]");

		// ���l���ꗗ�\���@��j���l[1,2,3]
		sb.append(" ���l[");
		for( int pos : possessedAgentNo ){
			sb.append(pos).append(",");
		}
		if( !possessedAgentNo.isEmpty() ){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]");

		wolfSideName = sb.toString();

	}

	/**
	 * ������
	 * @return �����񉻂����T�w�c
	 */
	public String toString(){
		return wolfSideName;
	}

}
