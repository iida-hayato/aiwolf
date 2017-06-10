package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.guess.AnalysisOfGuess;
import jp.iida.hayato.aiwolf.lib.AdvanceGameInfo;
import jp.iida.hayato.aiwolf.lib.AgentParameter;
import jp.iida.hayato.aiwolf.lib.ViewpointInfo;

/**
 * �s����p�ւ̈����N���X
 */
public final class ActionStrategyArgs {

	/** ������� */
	public AdvanceGameInfo agi;

	/** ���_��� */
	public ViewpointInfo view;

	/** ������� */
	public AnalysisOfGuess aguess;

	/** �l�̎����Ă����� */
	public AgentParameter parsonalData;

}
