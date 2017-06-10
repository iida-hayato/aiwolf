package jp.iida.hayato.aiwolf.strategyplayer;

import java.util.ArrayList;
import java.util.List;

public class RequestManager {


	/** �S�Ă̍s���v�� */
	public List<ReceivedRequest> allRequest = new ArrayList<ReceivedRequest>();



	/**
	 * �s���v����ǉ�
	 * @param guess �ǉ�����s���v��
	 */
	public void addRequest(ReceivedRequest guess){

		// �S�Ă̍s���v���Ƃ��Ēǉ�
		allRequest.add(guess);

	}

	/**
	 * �s���v����ǉ�
	 * @param guesses �ǉ�����s���v���̃��X�g
	 */
	public void addRequest(List<ReceivedRequest> requests){

		// �P�̒ǉ����\�b�h���g���A�P���ǉ�
		for(ReceivedRequest request: requests){
			addRequest(request);
		}

	}









}
