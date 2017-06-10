package jp.iida.hayato.aiwolf.lib;

import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.net.GameSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * ���_����\���N���X
 */
public final class ViewpointInfo {

	/** ���̎��_�ő��݂�����A�T�w�c�̑g�ݍ��킹�p�^�[�� */
	public HashMap<String, WolfsidePattern> wolfsidePatterns;

	/** ���̎��_����܂��鎋�_ */
	public List<ViewpointInfo> inclusionViewpoint = new ArrayList<ViewpointInfo>();



	/** �L���b�V����񂪗L���� */
	private boolean isCacheEnable;


	/** �T�̃p�^�[�������݂��Ȃ��G�[�W�F���g�ꗗ */
	private List<Integer> notWolfAgentNo = new ArrayList<Integer>();

	/** �T/��T�̃p�^�[�����������݂���G�[�W�F���g�ꗗ */
	private List<Integer> unclearWolfAgentNo = new ArrayList<Integer>();

	/** �T�̃p�^�[���݂̂����݂���G�[�W�F���g�ꗗ */
	private List<Integer> fixWolfAgentNo = new ArrayList<Integer>();


	/** ���̃p�^�[�������݂��Ȃ��G�[�W�F���g�ꗗ */
	private List<Integer> notPossessedAgentNo = new ArrayList<Integer>();

	/** ��/�񋶂̃p�^�[�����������݂���G�[�W�F���g�ꗗ */
	private List<Integer> unclearPossessedAgentNo = new ArrayList<Integer>();

	/** ���̃p�^�[���݂̂����݂���G�[�W�F���g�ꗗ */
	private List<Integer> fixPossessedAgentNo = new ArrayList<Integer>();



	/** �l�O�̃p�^�[�������݂��Ȃ��G�[�W�F���g�ꗗ */
	private List<Integer> notWolfSideAgentNo = new ArrayList<Integer>();

	/** �l�O/��l�O�̃p�^�[�����������݂���G�[�W�F���g�ꗗ */
	private List<Integer> unclearWolfSideAgentNo = new ArrayList<Integer>();

	/** �l�O�̃p�^�[���݂̂����݂���G�[�W�F���g�ꗗ */
	private List<Integer> fixWolfSideAgentNo = new ArrayList<Integer>();






	/**
	 * �R���X�g���N�^
	 * @param gameSetting �Q�[���ݒ�
	 */
	public ViewpointInfo(GameSetting gameSetting){

		// �S���_�̘T�w�c�p�^�[�����쐬����
		setWolfSidePattern(gameSetting);

		// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
		isCacheEnable = false;

	}


	/**
	 * �R���X�g���N�^
	 * @param parent ���̎��_���܂��鎋�_
	 */
	public ViewpointInfo(ViewpointInfo parent){

		// �e���_�̘T�w�c���R�s�[����i�V�����[�R�s�[�j
		wolfsidePatterns = new HashMap<String, WolfsidePattern>(parent.wolfsidePatterns);

		// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
		isCacheEnable = false;

	}


	/**
	 * ���̎��_����܂��鎋�_��ǉ�����
	 * @param child
	 */
	public void addInclusionViewpoint(ViewpointInfo child){
		inclusionViewpoint.add(child);
	}


	/**
	 * ���_������蒼���i�e���_�ɑ�����j
	 * @param parent ���̎��_���܂��鎋�_
	 */
	public void remakePattern(ViewpointInfo parent){

		// �e���_�̘T�w�c���R�s�[����i�V�����[�R�s�[�j
		wolfsidePatterns = new HashMap<String, WolfsidePattern>(parent.wolfsidePatterns);

		// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
		isCacheEnable = false;

 		// ��܂��鎋�_���A���̎��_�����ɍ�蒼��
		for( ViewpointInfo child : inclusionViewpoint ){
			child.remakePattern(this);
		}

	}


	//TODO ���Ґ��Ή�
	/**
	 * �S���_�̘T�w�c�̑g�ݍ��킹�p�^�[����ݒ肷��
	 * @param gameSetting
	 */
 	private void setWolfSidePattern(GameSetting gameSetting){

 		HashMap<String, WolfsidePattern> patterns = new HashMap<String, WolfsidePattern>(5460, 1.0f);

		// �Q�T�P��
		if( gameSetting.getRoleNum(Role.WEREWOLF) == 2 && gameSetting.getRoleNum(Role.POSSESSED) == 1 ){
			// �TA
			for(int wolfAcnt=1; wolfAcnt <= gameSetting.getPlayerNum() - 2; wolfAcnt++){
				// �TB
				for(int wolfBcnt = wolfAcnt + 1; wolfBcnt <= gameSetting.getPlayerNum() - 1; wolfBcnt++){

					ArrayList<Integer> wolves = new ArrayList<Integer>();
					wolves.add(wolfAcnt);
					wolves.add(wolfBcnt);

					// ��
					for(int possessedcnt = 1; possessedcnt <= gameSetting.getPlayerNum(); possessedcnt++){
						if( possessedcnt != wolfAcnt && possessedcnt != wolfBcnt ){

							ArrayList<Integer> possessed = new ArrayList<Integer>();
							possessed.add(possessedcnt);

							WolfsidePattern pattern = new WolfsidePattern(wolves, possessed);
							patterns.put( pattern.getWolfSideCode(), pattern );
						}
					}
				}
			}
		}

		// �R�T�P��
		if( gameSetting.getRoleNum(Role.WEREWOLF) == 3 && gameSetting.getRoleNum(Role.POSSESSED) == 1 ){
			// �TA
			for(int wolfAcnt=1; wolfAcnt <= gameSetting.getPlayerNum() - 2; wolfAcnt++){
				// �TB
				for(int wolfBcnt = wolfAcnt + 1; wolfBcnt <= gameSetting.getPlayerNum() - 1; wolfBcnt++){
					// �TC
					for(int wolfCcnt = wolfBcnt + 1; wolfCcnt <= gameSetting.getPlayerNum(); wolfCcnt++){

						ArrayList<Integer> wolves = new ArrayList<Integer>();
						wolves.add(wolfAcnt);
						wolves.add(wolfBcnt);
						wolves.add(wolfCcnt);

						// ��
						for(int possessedcnt = 1; possessedcnt <= gameSetting.getPlayerNum(); possessedcnt++){
							if( possessedcnt != wolfAcnt && possessedcnt != wolfBcnt && possessedcnt != wolfCcnt ){

								ArrayList<Integer> possessed = new ArrayList<Integer>();
								possessed.add(possessedcnt);

								WolfsidePattern pattern = new WolfsidePattern(wolves, possessed);
								patterns.put( pattern.getWolfSideCode(), pattern );
							}
						}
					}
				}
			}
		}

		wolfsidePatterns = patterns;

	}


 	/**
 	 * ����̘T�w�c�p�^�[�����폜����
 	 * @param pattern
 	 */
 	private void removePattern(WolfsidePattern pattern, Iterator<WolfsidePattern> iter){

 		// ��܂��鎋�_����T�w�c�̃p�^�[�����폜����
		for( ViewpointInfo child : inclusionViewpoint ){
			child.removePattern(pattern);
		}

 		// ���̎��_����T�w�c�̃p�^�[�����폜����
		iter.remove();

		// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
		isCacheEnable = false;

 	}


 	/**
 	 * ����̘T�w�c�p�^�[�����폜����
 	 * @param pattern
 	 */
 	private void removePattern(WolfsidePattern pattern){

 		// ���̎��_����T�w�c�̃p�^�[�����폜����
		wolfsidePatterns.remove(pattern.getWolfSideCode());

 		// ��܂��鎋�_����T�w�c�̃p�^�[�����폜����
		for( ViewpointInfo child : inclusionViewpoint ){
			child.removePattern(pattern);
		}

		// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
		isCacheEnable = false;

 	}


	/**
	 * ����̃G�[�W�F���g���T�̃p�^�[�����폜����
	 * @param agentNo
	 */
	public void removeWolfPattern(int agentNo){

//		// �p�^�[�����폜����
//		if( wolfsidePatterns.keySet().removeIf(str -> str.charAt(agentNo) == WolfsidePattern.WOLF_CODE) ){
//			// �폜�����������ꍇ
//
//			// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
//			isCacheEnable = false;
//
//	 		// ��܂��鎋�_����T�w�c�̃p�^�[�����폜����
//			for( ViewpointInfo child : inclusionViewpoint ){
//				child.removeWolfPattern(agentNo);
//			}
//		}

		// �S�p�^�[���`�F�b�N
		Iterator<WolfsidePattern> iter = wolfsidePatterns.values().iterator();
		while( iter.hasNext() ){
			WolfsidePattern pattern = iter.next();

			// �w��G�[�W�F���g���T�̃p�^�[���ł���΍폜����
			if( pattern.isWolf(agentNo) ){
				removePattern(pattern, iter);
			}
		}

	}


	/**
	 * ����̃G�[�W�F���g���T�w�c�̃p�^�[�����폜����
	 * @param agentNo
	 */
	public void removeWolfsidePattern(int agentNo){

//		// �p�^�[�����폜����
//		if( wolfsidePatterns.keySet().removeIf(str -> str.charAt(agentNo) != WolfsidePattern.NOTWOLFSIDE_CODE) ){
//			// �폜�����������ꍇ
//
//			// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
//			isCacheEnable = false;
//
//	 		// ��܂��鎋�_����T�w�c�̃p�^�[�����폜����
//			for( ViewpointInfo child : inclusionViewpoint ){
//				child.removeWolfsidePattern(agentNo);
//			}
//		}

		// �S�p�^�[���`�F�b�N
		Iterator<WolfsidePattern> iter = wolfsidePatterns.values().iterator();
		while( iter.hasNext() ){
			WolfsidePattern pattern = iter.next();

			// �w��G�[�W�F���g���T�w�c�̃p�^�[���ł���΍폜����
			if( pattern.isWolfSide(agentNo) ){
				removePattern(pattern, iter);
			}
		}

	}


	/**
	 * ����̃G�[�W�F���g�����l�̃p�^�[���ȊO���폜����
	 * @param agentNo
	 */
	public void removeNotPossessedPattern(int agentNo){

//		// �p�^�[�����폜����
//		if( wolfsidePatterns.keySet().removeIf(str -> str.charAt(agentNo) != WolfsidePattern.POSSESSED_CODE) ){
//			// �폜�����������ꍇ
//
//			// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
//			isCacheEnable = false;
//
//	 		// ��܂��鎋�_����T�w�c�̃p�^�[�����폜����
//			for( ViewpointInfo child : inclusionViewpoint ){
//				child.removeNotPossessedPattern(agentNo);
//			}
//		}


		// �S�p�^�[���`�F�b�N
		Iterator<WolfsidePattern> iter = wolfsidePatterns.values().iterator();
		while( iter.hasNext() ){
			WolfsidePattern pattern = iter.next();

			// �w��G�[�W�F���g�����l�̃p�^�[���łȂ���΍폜����
			if( !pattern.isPossessed(agentNo) ){
				removePattern(pattern, iter);
			}
		}

	}


	/**
	 * ����̃G�[�W�F���g���T�w�c�łȂ��p�^�[�����폜����
	 * @param agentNo
	 */
	public void removeNotWolfsidePattern(int agentNo){

//		// �p�^�[�����폜����
//		if( wolfsidePatterns.keySet().removeIf(str -> str.charAt(agentNo) == WolfsidePattern.NOTWOLFSIDE_CODE) ){
//			// �폜�����������ꍇ
//
//			// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
//			isCacheEnable = false;
//
//	 		// ��܂��鎋�_����T�w�c�̃p�^�[�����폜����
//			for( ViewpointInfo child : inclusionViewpoint ){
//				child.removeNotWolfsidePattern(agentNo);
//			}
//		}

		// �S�p�^�[���`�F�b�N
		Iterator<WolfsidePattern> iter = wolfsidePatterns.values().iterator();
		while( iter.hasNext() ){
			WolfsidePattern pattern = iter.next();

			// �w��G�[�W�F���g���T�w�c�̃p�^�[���łȂ���΍폜����
			if( !pattern.isWolfSide(agentNo) ){
				removePattern(pattern, iter);
			}
		}

	}


	/**
	 * �P�l�������݂��Ȃ���E����̍i�荞�݁i�w��G�[�W�F���g�̂����A�������O�`�P���Ƃ��ē��󐮗�����j
	 * @param agentNo
	 */
	public void removePatternFromUniqueRole(List<Integer> agentNo){

		// �S�p�^�[���`�F�b�N
		Iterator<WolfsidePattern> iter = wolfsidePatterns.values().iterator();
		while( iter.hasNext() ){
			WolfsidePattern pattern = iter.next();

			// �w��G�[�W�F���g�ɘT�w�c�Ɋ܂܂Ȃ��҂��Q���ȏア��p�^�[�����폜
			boolean flag = false;
			for( int agent : agentNo ){
				if( !pattern.isWolfSide(agent) ){
					if( flag == false ){
						flag = true;
					}else{
						removePattern(pattern, iter);
						break;
					}
				}
			}
		}

	}


	/**
	 * ����O���[�v���̘T�w�c�l�����w�肵�A�����𖞂����Ȃ��p�^�[�����폜����
	 * @param agentNo �O���[�v�̃G�[�W�F���g�ԍ��ꗗ
	 * @param minnum �O���[�v���̍ŏ��T�w�c��
	 * @param maxnum �O���[�v���̍ő�T�w�c��
	 */
	public void removePatternFromWolfSideNum(List<Integer> agentNo, int minnum, int maxnum){

		// �S�p�^�[���`�F�b�N
		Iterator<WolfsidePattern> iter = wolfsidePatterns.values().iterator();
		while( iter.hasNext() ){
			WolfsidePattern pattern = iter.next();

			// �w��p�^�[���̘T�����J�E���g
			int count = 0;
			for( int agent : agentNo ){
				if( pattern.isWolfSide(agent) ){
					count++;
				}
			}
			// �w�肳�ꂽ�l������O��Ă���΃p�^�[�����폜
			if( count < minnum || count > maxnum ){
				removePattern(pattern, iter);
			}
		}

	}


	/**
	 * ����O���[�v���̘T�l�����w�肵�A�����𖞂����Ȃ��p�^�[�����폜����
	 * @param agentNo �O���[�v�̃G�[�W�F���g�ԍ��ꗗ(�\�[�g����̂ŕύX���Ă����Ȃ���)
	 * @param minnum �O���[�v���̍ŏ��T��
	 * @param maxnum �O���[�v���̍ő�T��
	 */
	public void removePatternFromWolfNum(List<Integer> agentNo, int minnum, int maxnum){

		// �G�[�W�F���g�ԍ��ꗗ�������\�[�g
		Collections.sort(agentNo);

		// �S�p�^�[���`�F�b�N
		Iterator<WolfsidePattern> iter = wolfsidePatterns.values().iterator();
		while( iter.hasNext() ){
			WolfsidePattern pattern = iter.next();

			// �w��p�^�[���̘T�����J�E���g
			int count = 0;
//			for( int agent : agentNo ){
//				if( pattern.isWolf(agent) ){
//					count++;
//				}
//			}
			List<Integer> wolves = pattern.wolfAgentNo;
			int listIdx = 0;
			int patternIdx = 0;
			int listVal;
			int patternVal;
			while( listIdx < agentNo.size() && patternIdx < wolves.size() ){
				listVal = agentNo.get(listIdx);
				patternVal = wolves.get(patternIdx);
				if( listVal < patternVal ){
					listIdx++;
				}else if( listVal > patternVal ){
					patternIdx++;
				}else{
					count++;
					listIdx++;
					patternIdx++;
				}
			}

			// �w�肳�ꂽ�l������O��Ă���΃p�^�[�����폜
			if( count < minnum || count > maxnum ){
				removePattern(pattern, iter);
			}
		}

	}


	/**
	 * ���肩��̃p�^�[���i�荞��
	 * @param agentNo ������o��������No
	 * @param targetno ����Ώۂ�No
	 * @param result ���茋��
	 */
	public void removePatternFromJudge(int agentNo, int targetno, Species result){


//		if( result == Species.HUMAN ){
//			// �l�Ԕ��� �� �ΏۃG�[�W�F���g���T�̃p�^�[�����폜����
//
//			// �p�^�[�����폜����
//			if( wolfsidePatterns.keySet().removeIf(str -> ( str.charAt(agentNo) == WolfsidePattern.NOTWOLFSIDE_CODE &&
//					                                        str.charAt(targetno) == WolfsidePattern.WOLF_CODE ) )){
//				// �폜�����������ꍇ
//
//				// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
//				isCacheEnable = false;
//
//		 		// ��܂��鎋�_����T�w�c�̃p�^�[�����폜����
//				for( ViewpointInfo child : inclusionViewpoint ){
//					child.removePatternFromJudge(agentNo, targetno, result);
//				}
//			}
//
//		}else{
//			// �l�T���� �� �ΏۃG�[�W�F���g���T�łȂ��p�^�[�����폜����
//
//			// �p�^�[�����폜����
//			if( wolfsidePatterns.keySet().removeIf(str -> ( str.charAt(agentNo) == WolfsidePattern.NOTWOLFSIDE_CODE &&
//					                                        str.charAt(targetno) != WolfsidePattern.WOLF_CODE ) )){
//				// �폜�����������ꍇ
//
//				// �p�^�[���ɕύX���o�����߁A�L���b�V�����𖳌��ɂ���
//				isCacheEnable = false;
//
//		 		// ��܂��鎋�_����T�w�c�̃p�^�[�����폜����
//				for( ViewpointInfo child : inclusionViewpoint ){
//					child.removePatternFromJudge(agentNo, targetno, result);
//				}
//			}
//		}

		// �S�p�^�[���`�F�b�N
		Iterator<WolfsidePattern> iter = wolfsidePatterns.values().iterator();
		while( iter.hasNext() ){
			WolfsidePattern pattern = iter.next();

			// ������o������삪�l�O�̃p�^�[���͑ΏۊO
			if( pattern.isWolfSide(agentNo) ){
				continue;
			}

			// �����肩
			if( result == Species.WEREWOLF ){
				// ������ �� �ΏۃG�[�W�F���g���T�̃p�^�[���łȂ���΍폜����
				if( !pattern.isWolf(targetno) ){
					removePattern(pattern, iter);
				}
			}else{
				// ������ �� �ΏۃG�[�W�F���g���T�̃p�^�[���ł���΍폜����
				if( pattern.isWolf(targetno) ){
					removePattern(pattern, iter);
				}
			}

		}

	}


	/**
	 * ���_���̃L���b�V�����쐬����
	 */
	private void makeCache(){

		// ���E�D�E���̈ꗗ���N���A
		notWolfAgentNo.clear();
		unclearWolfAgentNo.clear();
		fixWolfAgentNo.clear();

		//TODO ���Ґ��Ή�
		int wolfCount[] = new int[15+1];
		int possessedCount[] = new int[15+1];

		// �ePL���T�E���l�Ƃ��Ċ܂܂��p�^�[���̐����J�E���g
		for( WolfsidePattern pattern : wolfsidePatterns.values() ){
			for( int wolf : pattern.wolfAgentNo ){
				wolfCount[wolf]++;
			}
			for( int pos : pattern.possessedAgentNo ){
				possessedCount[pos]++;
			}
		}

		// �p�^�[���̐��ɂ���Ĕ��E�D�E���̈ꗗ�ɐU�蕪����
		for( int i = 1; i <= 15; i++ ){

			// �T�n
			if( wolfCount[i] == 0 ){
				notWolfAgentNo.add(i);
			}else if( wolfCount[i] == wolfsidePatterns.size() ){
				fixWolfAgentNo.add(i);
			}else{
				unclearWolfAgentNo.add(i);
			}

			// ���l�n
			if( possessedCount[i] == 0 ){
				notPossessedAgentNo.add(i);
			}else if( possessedCount[i] == wolfsidePatterns.size() ){
				fixPossessedAgentNo.add(i);
			}else{
				unclearPossessedAgentNo.add(i);
			}

			// �l�O�n
			if( wolfCount[i] + possessedCount[i] == 0 ){
				notWolfSideAgentNo.add(i);
			}else if( wolfCount[i] + possessedCount[i] == wolfsidePatterns.size() ){
				fixWolfSideAgentNo.add(i);
			}else{
				unclearWolfSideAgentNo.add(i);
			}

		}

		// �L���b�V����L���ɂ���
		isCacheEnable = true;

	}


	/**
	 * ����̃G�[�W�F���g���m�荕�����擾����
	 * @param agentNo
	 * @return
	 */
	public boolean isFixBlack(int agentNo){

		// �L���b�V����񂪗L���łȂ���΍쐬����
		if( !isCacheEnable ){
			makeCache();
		}

		// �m�荕�̃��X�g�Ɋ܂܂��Ίm�荕
		if( fixWolfAgentNo.indexOf(agentNo) != -1 ){
			return true;
		}

		// �m�荕�ł͂Ȃ�
		return false;

	}


	/**
	 * ����̃G�[�W�F���g���m�蔒�����擾����
	 * @param agentNo
	 * @return
	 */
	public boolean isFixWhite(int agentNo){

		// �L���b�V����񂪗L���łȂ���΍쐬����
		if( !isCacheEnable ){
			makeCache();
		}

		// �m�荕�̃��X�g�Ɋ܂܂��Ίm�荕
		if( notWolfAgentNo.indexOf(agentNo) != -1 ){
			return true;
		}

		// �m�荕�ł͂Ȃ�
		return false;

	}


	/**
	 * ����̃G�[�W�F���g���m��l�O�����擾����
	 * @param agentNo
	 * @return
	 */
	public boolean isFixWolfSide(int agentNo){

		// �L���b�V����񂪗L���łȂ���΍쐬����
		if( !isCacheEnable ){
			makeCache();
		}

		// �m��l�O�̃��X�g�Ɋ܂܂��Ίm��l�O
		if( fixWolfSideAgentNo.indexOf(agentNo) != -1 ){
			return true;
		}

		// �m��l�O�ł͂Ȃ�
		return false;

	}


	/**
	 * ���m��̃G�[�W�F���g�ꗗ���擾����
	 * @return
	 */
	public List<Integer> getFixBlackAgent(){

		// �L���b�V����񂪗L���łȂ���΍쐬����
		if( !isCacheEnable ){
			makeCache();
		}

		return fixWolfAgentNo;

	}


	/**
	 * ���m��̃G�[�W�F���g�ꗗ���擾����
	 * @return
	 */
	public List<Integer> getFixWhiteAgent(){

		// �L���b�V����񂪗L���łȂ���΍쐬����
		if( !isCacheEnable ){
			makeCache();
		}

		return notWolfAgentNo;

	}

}
