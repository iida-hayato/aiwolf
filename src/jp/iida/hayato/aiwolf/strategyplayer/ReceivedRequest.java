package jp.iida.hayato.aiwolf.strategyplayer;

import jp.iida.hayato.aiwolf.request.AbstractActionStrategy;
import jp.iida.hayato.aiwolf.request.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * �e�s����p�N���X����󂯎�����s���v��
 */
public class ReceivedRequest {

	/** �s���v���̓��e */
	public Request request;

	/** �v�����s������p */
	public AbstractActionStrategy strategy;

	/** �v���̏d��(�v���W��^�v���̏d��)�@�S��̗v���ɑ΂��Ă����� */
	public double weight = 1.0;


	public ReceivedRequest(Request request, AbstractActionStrategy strategy){
		this.request = request;
		this.strategy = strategy;
	}

	public ReceivedRequest(Request request, AbstractActionStrategy strategy, double weight){
		this.request = request;
		this.strategy = strategy;
		this.weight = weight;
	}

	public static List<ReceivedRequest> newRequests(ArrayList<Request> requests, AbstractActionStrategy strategy){

		ArrayList<ReceivedRequest> rguesses = new ArrayList<ReceivedRequest>();

		for(Request request: requests){
			ReceivedRequest rrequest = new ReceivedRequest(request, strategy);
			rguesses.add(rrequest);
		}

		return rguesses;

	}

	public static List<ReceivedRequest> newRequests(ArrayList<Request> requests, AbstractActionStrategy strategy, double weight){

		ArrayList<ReceivedRequest> rguesses = new ArrayList<ReceivedRequest>();

		for(Request request: requests){
			ReceivedRequest rrequest = new ReceivedRequest(request, strategy, weight);
			rguesses.add(rrequest);
		}

		return rguesses;

	}

}
