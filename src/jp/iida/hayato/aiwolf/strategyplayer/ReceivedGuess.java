package jp.iida.hayato.aiwolf.strategyplayer;

import jp.iida.hayato.aiwolf.guess.AbstractGuessStrategy;
import jp.iida.hayato.aiwolf.guess.Guess;

import java.util.ArrayList;
import java.util.List;

/**
 * �e������p�N���X����󂯎��������
 */
public final class ReceivedGuess {

	/** �����̓��e */
	public Guess guess;

	/** �������s������p */
	public AbstractGuessStrategy strategy;

	/** �����̏d��(�W��^�����̏d��) */
	public double weight = 1.0;


	public ReceivedGuess(Guess guess, AbstractGuessStrategy strategy){
		this.guess = guess;
		this.strategy = strategy;
	}


	public ReceivedGuess(Guess guess, AbstractGuessStrategy strategy, double weight){
		this.guess = guess;
		this.strategy = strategy;
		this.weight = weight;
	}

	public static List<ReceivedGuess> newGuesses(ArrayList<Guess> guesses, AbstractGuessStrategy strategy){

		ArrayList<ReceivedGuess> rguesses = new ArrayList<ReceivedGuess>();

		for(Guess guess: guesses){
			ReceivedGuess rguess = new ReceivedGuess(guess, strategy);
			rguesses.add(rguess);
		}

		return rguesses;

	}

	public static List<ReceivedGuess> newGuesses(ArrayList<Guess> guesses, AbstractGuessStrategy strategy, double weight){

		ArrayList<ReceivedGuess> rguesses = new ArrayList<ReceivedGuess>();

		for(Guess guess: guesses){
			ReceivedGuess rguess = new ReceivedGuess(guess, strategy, weight);
			rguesses.add(rguess);
		}

		return rguesses;

	}

}
