package jp.iida.hayato.aiwolf.lib;


import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * ���ʊ֐�
 */
public final class Common {

	/**
	 * �R���X�g���N�^(�C���X�^���X���֎~)
	 */
	private Common(){}



	/**
	 * �c�菈�Y�񐔂̎擾�i�ŏI���܂ő����O��j
	 * @param aliveAgentNum �����G�[�W�F���g��
	 * @return �c�菈�Y��
	 */
	public static int getRestExecuteCount(int aliveAgentNum){

		// (�����Ґ� - 1) / 2 �i�����؎̂āj���c�菈�Y��
		return (aliveAgentNum - 1) / 2;

	}


	/**
	 * �G�[�W�F���g�ԍ��̃��X�g���擾����
	 * @param agents
	 * @return
	 */
	public static List<Integer> getAgentNo(List<Agent> agents){

		List<Integer> ret = new ArrayList<Integer>();

		for( Agent agent : agents ){
			ret.add(agent.getAgentIdx());
		}

		return ret;

	}


	/**
	 * �Q�̔����̎��n����擾����
	 * @param day1 �����P�̓�
	 * @param talkid1 �����P�̔���ID
	 * @param day2 �����Q�̓�
	 * @param talkid2 �����Q�̔���ID
	 * @return -1:����1����@0:�����@1:����2����
	 */
	public static int compareTalkID( int day1, int talkid1, int day2, int talkid2 ){

		// �����P���悩
		if( day1 < day2 || ( day1 == day2 && talkid1 < talkid2 ) ){
			return -1;
		}

		// �����Q���悩
		if( day1 > day2 || ( day1 == day2 && talkid1 > talkid2 ) ){
			return 1;
		}

		// ����
		return 0;

	}


	/**
	 * �ő��[�𓾂��҂�No���擾����i���[���͕����擾�j
	 * @param voteList ���[����
	 * @return
	 */
	public static List<Integer> getMaxVoteAgentNo(List<Vote> voteList){

		// �G�[�W�F���g���̓��[�����擾����}�b�v key=AgentNo value=�[��
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

		// ���[�����J�E���g
		for( Vote vote : voteList ){
			int target = vote.getTarget().getAgentIdx();

			if( map.containsKey(target) ){
				map.put(target, map.get(target) + 1);
			}else{
				map.put(target, 1);
			}
		}

		// �ő�̓��[�����擾
		int maxVoteCount = 0;
		for( Entry<Integer, Integer> entry : map.entrySet() ){
			if( entry.getValue() > maxVoteCount ){
				maxVoteCount = entry.getValue();
			}
		}

		// �ő�̓��[���̃G�[�W�F���g�����X�g������
		List<Integer> ret = new ArrayList<Integer>();
		for( Entry<Integer, Integer> entry : map.entrySet() ){
			if( entry.getValue() == maxVoteCount ){
				ret.add( entry.getKey() );
			}
		}

		return ret;

	}


}
