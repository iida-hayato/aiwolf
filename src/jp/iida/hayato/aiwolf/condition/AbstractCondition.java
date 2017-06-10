package jp.iida.hayato.aiwolf.condition;

import jp.iida.hayato.aiwolf.lib.WolfsidePattern;

import java.util.ArrayList;


/**
 * 条件を表す抽象クラス
 */
public abstract class AbstractCondition {


	/**
	 * 条件を満たすか
	 * @return
	 */
	abstract public boolean isMatch( WolfsidePattern pattern );


	/**
	 * 対象となるエージェントの番号一覧を取得する
	 * @return
	 */
	abstract public ArrayList<Integer> getTargetAgentNo();


}
