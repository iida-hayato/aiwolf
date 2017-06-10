package jp.iida.hayato.aiwolf.request;


import java.util.ArrayList;


/**
 * �s����p��\�����ۃN���X�B
 */
public abstract class AbstractActionStrategy {

	/**
	 * �v������s���̃��X�g���擾����B
	 * @param args �����Z�b�g
	 * @return �v������s���̃��X�g
	 */
	public abstract ArrayList<Request> getRequests(ActionStrategyArgs args);


}
