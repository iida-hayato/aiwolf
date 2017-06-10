package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.strategyplayer.ReceivedGuess;

import java.util.ArrayList;
import java.util.List;

public final class GuessManager {



	/** �S�Ă̐��� */
	private List<ReceivedGuess> allGuess = new ArrayList<ReceivedGuess>();

	/** �e�G�[�W�F���g�P�̂ɑ΂��鐄�� */
	private List<List<ReceivedGuess>> guessForSingleAgent = new ArrayList<List<ReceivedGuess>>();

	/** �����G�[�W�F���g�̑g�ݍ��킹�ɑ΂��鐄�� */
	private List<ReceivedGuess> guessForMultiAgent = new ArrayList<ReceivedGuess>();


	/**
	 * �R���X�g���N�^
	 * @param agentNum �G�[�W�F���g��
	 */
	public GuessManager(int agentNum){

		for( int i = 0; i <= agentNum; i++ ){
			guessForSingleAgent.add(new ArrayList<ReceivedGuess>());
		}

	}

	/**
	 * ������ǉ�
	 * @param guess �ǉ����鐄��
	 */
	public void addGuess(ReceivedGuess guess){

		// �S�Ă̐����Ƃ��Ēǉ�
		allGuess.add(guess);

		// �p�^�[���Ώۂ̃G�[�W�F���g�ԍ��ꗗ���擾����
		ArrayList<Integer> TargetAgents = guess.guess.condition.getTargetAgentNo();

		// �O��E���ۂ̃G�[�W�F���g�����v���P�ȉ���
		if( TargetAgents.size()  <= 1 ){
			// �P�ƃG�[�W�F���g�ɑ΂��鐄���Ƃ��Ēǉ�
			int agentno = TargetAgents.get(0);
			guessForSingleAgent.get(agentno).add(guess);
		}else{
			// �����G�[�W�F���g�̑g�ݍ��킹�ɑ΂��鐄���Ƃ��Ēǉ�
			guessForMultiAgent.add(guess);
		}

	}

	/**
	 * ������ǉ�
	 * @param guesses �ǉ����鐄���̃��X�g
	 */
	public void addGuess(List<ReceivedGuess> guesses){

		// �P�̒ǉ����\�b�h���g���A�P���ǉ�
		for(ReceivedGuess guess: guesses){
			addGuess(guess);
		}

	}


	/**
	 * �G�[�W�F���g�P�̂ɑ΂��鐄�����擾
	 * @param agentno �G�[�W�F���g�ԍ�
	 * @return
	 */
	public List<ReceivedGuess> getGuessForSingleAgent(int agentno){
		return guessForSingleAgent.get(agentno);
	}


	/**
	 * �����G�[�W�F���g�ɑ΂��鐄�����擾
	 * @return
	 */
	public List<ReceivedGuess> getGuessForMultiAgent(){
		return guessForMultiAgent;
	}



}
