package jp.iida.hayato.aiwolf.guess;

import java.util.ArrayList;


/**
 * ������p��\�����ۃN���X
 */
public abstract class AbstractGuessStrategy {

	/**
	 * �����̃��X�g���擾����B
	 * @param args �����Z�b�g
	 * @return �����̃��X�g
	 */
	public abstract ArrayList<Guess> getGuessList(GuessStrategyArgs args);


}
