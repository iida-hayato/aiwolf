package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.lib.WolfsidePattern;
import jp.iida.hayato.aiwolf.strategyplayer.ReceivedGuess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * �����̕��͌��ʂ�\���N���X
 */
public final class AnalysisOfGuess {


	/** �S�p�^�[���̕��͌��� */
	private HashMap<String, InspectedWolfsidePattern> allPattern;

	/** �G�[�W�F���g���̒P�̘T�v�f���͌��� */
	private List<InspectedWolfsidePattern> singleAgentWolfPattern = new ArrayList<InspectedWolfsidePattern>();

	/** �G�[�W�F���g���̒P�̋��l�v�f���͌��� */
	private List<InspectedWolfsidePattern> singleAgentPossessedPattern = new ArrayList<InspectedWolfsidePattern>();

	/** �G�[�W�F���g���̍ł��Ó��ȘT�p�^�[�� idx=AgentNo */
	private ArrayList<InspectedWolfsidePattern> mostWolfPattern = new ArrayList<InspectedWolfsidePattern>();

	/** �G�[�W�F���g���̍ł��Ó��ȋ��l�p�^�[�� idx=AgentNo */
	private ArrayList<InspectedWolfsidePattern> mostPossessedPattern = new ArrayList<InspectedWolfsidePattern>();

	/** ��̘T�w�c�p�^�[��(Null�΍�) */
	private static final InspectedWolfsidePattern emptyWolfsidePattern = new InspectedWolfsidePattern(new WolfsidePattern(new ArrayList<Integer>(), new ArrayList<Integer>()), 0.0);


	//TODO �������ł̓f�[�^�\���݂̂ɂ��āA�i�[�͏�ʃ��W���[���ł��ׂ��H�N���X�̃p�b�P�[�W�ړ�������(ReceivedGuess���z�Q��)
	/**
	 * �R���X�g���N�^
	 * @param patterns �T�w�c�̃p�^�[��
	 * @param guessmanager ��������
	 */
	public AnalysisOfGuess(int agentNum, Collection<WolfsidePattern> patterns, GuessManager guessManager) {

		// �S�p�^�[���̕��͌��� �������e�ʂ��w�肵�ď�����
		allPattern = new HashMap<String, InspectedWolfsidePattern>(patterns.size() * 4 / 3);

		// �X�̃G�[�W�F���g�P�̂ŘT�W���E���l�W�������߂�
		ArrayList<Double> singleWolfScore = new ArrayList<Double>();
		ArrayList<Double> singlePossessedScore = new ArrayList<Double>();
		// 1�I���W���ɂ��邽��0�Ԃ̗v�f�Ƀ_�~�[��ݒ�
		singleWolfScore.add(null);
		singlePossessedScore.add(null);
		for(int i = 1; i <= agentNum; i++){
			// �W���̏�����
			double wolfScore = 1.0;
			double possessedScore = 1.0;

			// �_�~�[�p�^�[���̍쐬
			ArrayList<Integer> singleAgent = new ArrayList<Integer>();
			singleAgent.add(i);
			ArrayList<Integer> blank = new ArrayList<Integer>();
			WolfsidePattern wolfPattern = new WolfsidePattern(singleAgent, blank);
			WolfsidePattern posPattern = new WolfsidePattern(blank, singleAgent);

			List<ReceivedGuess> singleGuesses = guessManager.getGuessForSingleAgent(i);
			// �����̑���
			for(ReceivedGuess rguess : singleGuesses ){
				// �����̓���p�^�[�����T�p�^�[���ƃ}�b�`���邩
				if( rguess.guess.condition.isMatch(wolfPattern) ){
					wolfScore *= Math.pow( rguess.guess.correlation, rguess.weight);
				}
				// �����̓���p�^�[�������l�p�^�[���ƃ}�b�`���邩
				if( rguess.guess.condition.isMatch(posPattern) ){
					possessedScore *= Math.pow( rguess.guess.correlation, rguess.weight);
				}
			}

			// �P�̃p�^�[���Ƃ��ċL��
			singleWolfScore.add(wolfScore);
			singlePossessedScore.add(possessedScore);
			InspectedWolfsidePattern inspectedWolfPattern = new InspectedWolfsidePattern(wolfPattern, wolfScore);
			InspectedWolfsidePattern inspectedPosPattern = new InspectedWolfsidePattern(posPattern, possessedScore);
			inspectedWolfPattern.guesses = singleGuesses;
			inspectedPosPattern.guesses = singleGuesses;
			singleAgentWolfPattern.add( inspectedWolfPattern );
			singleAgentPossessedPattern.add( inspectedPosPattern );
		}

		// �G�[�W�F���g���̍ł��Ó��ȃp�^�[���������o�^
		for(int i = 0; i <= agentNum; i++){
			mostWolfPattern.add(emptyWolfsidePattern);
			mostPossessedPattern.add(emptyWolfsidePattern);
		}

		// �T�w�c�p�^�[���̑���
		Iterator<WolfsidePattern> iter = patterns.iterator();
		while( iter.hasNext() ){
			WolfsidePattern pattern = iter.next();

			// ���̃p�^�[���Ɋ֘A���鐄��
			//List<ReceivedGuess> guesses = new ArrayList<ReceivedGuess>();

			double score = 1.0;
			// �e�T�̒P�̌W���̌v�Z
			for( int wolfAgentNo : pattern.wolfAgentNo ){
				score *= singleWolfScore.get(wolfAgentNo);
			}
			// �e���l�̒P�̌W���̌v�Z
			for( int posAgentNo : pattern.possessedAgentNo ){
				score *= singlePossessedScore.get(posAgentNo);
			}
			// �����̑���
			for(ReceivedGuess rguess : guessManager.getGuessForMultiAgent() ){
				// �����̏������T�w�c�p�^�[���ƃ}�b�`���邩
				if( rguess.guess.condition.isMatch(pattern) ){
					// �T�w�c�̃X�R�A��␳����
					score *= Math.pow( rguess.guess.correlation, rguess.weight);

					// ���̃p�^�[���Ɋ֘A���鐄�����L��
					//guesses.add(rguess);
				}
			}
			// �T�w�c�p�^�[���ɑ΂��錟�،��ʂ��i�[
			InspectedWolfsidePattern inspectedPattern = new InspectedWolfsidePattern(pattern, score);
			//inspectedPattern.guesses = guesses;

			addPattern(inspectedPattern);

			// �e�G�[�W�F���g�̘T/���l�Ƃ��čł��Ó��ȃp�^�[���𒀎��v�Z
			for( int wolfAgentNo : pattern.wolfAgentNo ){
				if( score > mostWolfPattern.get(wolfAgentNo).score ){
					mostWolfPattern.set(wolfAgentNo, inspectedPattern);
				}
			}
			for( int posAgentNo : pattern.possessedAgentNo ){
				if( score > mostPossessedPattern.get(posAgentNo).score ){
					mostPossessedPattern.set(posAgentNo, inspectedPattern);
				}
			}

		}


	}


	/**
	 * ���،��ʂ̋L��
	 * @param pattern
	 */
	public void addPattern(InspectedWolfsidePattern pattern){

		// ���،��ʂ̋L��
		allPattern.put(pattern.pattern.getWolfSideCode(), pattern);

	}


	/**
	 * ����̘T�w�c�Ɋւ��镪�͌��ʂ��擾����
	 * @return �w�肵���T�w�c�Ɋւ��镪�͌���
	 */
	public InspectedWolfsidePattern getPattern(WolfsidePattern pattern){

		return allPattern.get(pattern.getWolfSideCode());

	}


	//TODO ���\�b�h�ɂ���ă_�~�[��Ԃ��d�l��Null��Ԃ��d�l�����݂��ĂċC���������B���ꂷ��H
	/**
	 * �ł��Ó��ȘT�w�c�Ɋւ��镪�͌��ʂ��擾����
	 * @return �ł��Ó��ȘT�w�c�Ɋւ��镪�͌���(����X�R�A������ꍇ�A���Ԃ͕ۏ؂���Ȃ�)�@��������score0�̃_�~�[��Ԃ�
	 */
	public InspectedWolfsidePattern getMostValidPattern(){

		InspectedWolfsidePattern mostValidWolfsidePattern = emptyWolfsidePattern;
		double mostValidWolfScore = Double.NEGATIVE_INFINITY;

		// �T�w�c�p�^�[���̑���
		for( InspectedWolfsidePattern pattern : mostWolfPattern ){
			// �ő�X�R�A�ł���ΐw�c���L������
			if( pattern != null && pattern.score > mostValidWolfScore ){
				mostValidWolfsidePattern = pattern;
				mostValidWolfScore = pattern.score;
			}
		}

		return mostValidWolfsidePattern;

	}


	/**
	 * ����̃G�[�W�F���g���T�̃p�^�[���̂����A�ł��Ó��ȘT�w�c�Ɋւ��镪�͌��ʂ��擾����
	 * @param agentNo �G�[�W�F���g�ԍ�
	 * @return �ł��Ó��ȘT�w�c�Ɋւ��镪�͌���(����X�R�A������ꍇ�A���Ԃ͕ۏ؂���Ȃ�)�@��������score0�̃_�~�[��Ԃ�
	 */
	public InspectedWolfsidePattern getMostValidWolfPattern(int agentNo){

		return mostWolfPattern.get(agentNo);

	}


	/**
	 * ����̃G�[�W�F���g�����l�̃p�^�[���̂����A�ł��Ó��ȘT�w�c�Ɋւ��镪�͌��ʂ��擾����
	 * @param agentNo �G�[�W�F���g�ԍ�
	 * @return �ł��Ó��ȘT�w�c�Ɋւ��镪�͌���(����X�R�A������ꍇ�A���Ԃ͕ۏ؂���Ȃ�)�@��������score0�̃_�~�[��Ԃ�
	 */
	public InspectedWolfsidePattern getMostValidPossessedPattern(int agentNo){

		return mostPossessedPattern.get(agentNo);

	}


	/**
	 * �G�[�W�F���g�P�̂Ɋւ���l�T�̕��͌��ʂ��擾����
	 * @param agentNo �G�[�W�F���g�ԍ�
	 * @return
	 */
	public InspectedWolfsidePattern getSingleWolfPattern(int agentNo){

		return singleAgentPossessedPattern.get(agentNo - 1);

	}


	/**
	 * �G�[�W�F���g�P�̂Ɋւ��鋶�l�̕��͌��ʂ��擾����
	 * @param agentNo �G�[�W�F���g�ԍ�
	 * @return
	 */
	public InspectedWolfsidePattern getSinglePossessedPattern(int agentNo){

		return singleAgentPossessedPattern.get(agentNo - 1);

	}


	/**
	 * �S�p�^�[���̕��͌��ʂ��擾����
	 * @return
	 */
	public HashMap<String, InspectedWolfsidePattern> getAllPattern(){
		return allPattern;
	}

}
