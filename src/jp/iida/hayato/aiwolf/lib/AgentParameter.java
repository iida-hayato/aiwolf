package jp.iida.hayato.aiwolf.lib;

import java.util.HashMap;

/**
 * �l�Ɋւ���f�[�^���Ǘ�����N���X
 */
public class AgentParameter {

	private HashMap<String, Double> param = new HashMap<String, Double>();


	/**
	 * �p�����[�^�̎擾
	 * @param key ���ږ�
	 * @param defaultValue ���ڂ����݂��Ȃ��ꍇ�̖߂�l
	 * @return
	 */
	public Double getParam(String key, Double defaultValue){
		if( param.containsKey(key) ){
			return param.get(key);
		}
		return defaultValue;
	}

	/**
	 * �p�����[�^�̐ݒ�
	 * @param key ���ږ�
	 * @param value�@�l
	 * @param multiplication ���ɍ��ڂ�����ꍇ�A��Z����
	 */
	public void setParam(String key, Double value, boolean multiplication){

		if( param.containsKey(key) ){
			// ���ڂ����݂���ꍇ�A�㏑��or��Z
			if( multiplication ){
				param.put(key, param.get(key) * value);
			}else{
				param.put(key, value) ;
			}
		}else{
			// ���ڂ����݂��Ȃ��ꍇ���̂܂ܐݒ�
			param.put(key, value) ;
		}

	}


}
