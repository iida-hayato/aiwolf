package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.guess.InspectedWolfsidePattern;
import jp.iida.hayato.aiwolf.lib.ComingOut;

import java.util.ArrayList;
import java.util.List;


/**
 * �s����p�uPP(���l��)�v
 */
public final class PowerPlay_Possessed extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;


		// PP�˓��ςłȂ���΋󃊃X�g��Ԃ�
		if( !args.agi.isEnablePowerPlay_Possessed() ){
			return Requests;
		}



		// �[�����킹��G�[�W�F���g���擾
		Integer mostWolfAgentNo = null;

		// �������l���_�̊m��T���擾�i�擪�P���j
		for( int i = 1; i < args.agi.gameSetting.getPlayerNum(); i++ ){
			if( args.agi.selfRealRoleViewInfo.isFixBlack(i) ){
				mostWolfAgentNo = i;
				break;
			}
		}

		// �m��T�����Ȃ�
		if( mostWolfAgentNo == null ){
			// �l�O�b�n�����҂�����ꍇ�A�ł������b�n�����҂ɓ��[�����킹��
			for( ComingOut co :args.agi.wolfsideComingOutList ){
				if( co.isEnable() && !args.agi.selfRealRoleViewInfo.isFixWhite(co.agentNo) ){
					mostWolfAgentNo = co.agentNo;
					break;
				}
			}

		}

		//TODO �m��T�ɂ��킹��i�ρj���b�n�T�ɍ��킹��i�ρj���m�聛�ɓ��[�i�ρj���N�����P�[�ɂ���H�i���j�������̋t�ɓ��[�i���j

		// �m��T������ꍇ�A�m��T�ɓ��[�����킹��
		if( mostWolfAgentNo != null ){

			Integer target = args.agi.getSaidVoteAgent(mostWolfAgentNo);

			if( target != null && target != args.agi.latestGameInfo.getAgent().getAgentIdx() ){
				workReq = new Request( target );
				workReq.vote = 1000000.0;

				// �s���v���̓o�^
				Requests.add(workReq);

				return Requests;
			}
		}




		// �l�Ԋm��̃G�[�W�F���g���擾
		List<Integer> fixHumanAgentNo = new ArrayList<Integer>();

		// �������l���_�̊m��l�Ԃ��擾
		for( int i = 1; i < args.agi.gameSetting.getPlayerNum(); i++ ){
			if( args.agi.selfRealRoleViewInfo.isFixWhite(i) ){
				fixHumanAgentNo.add(i);
			}
		}

		// �m��l�Ԃ�����ꍇ�A�m��l�Ԃɓ��[����
		if( !fixHumanAgentNo.isEmpty() ){
			for( int human : fixHumanAgentNo ){
				workReq = new Request( human );
				workReq.vote = 1000000.0;

				// �s���v���̓o�^
				Requests.add(workReq);
			}

			return Requests;
		}






		//TODO �������l���_�Ő������Ȃ��ƃX�R�A�������i�d����Β��߂�H�j

		// �P�̍l�@�Ɛw�c�l�@�̏d�݂����߂�i���Ղ͔��X���x�ŁA�I�Ղ͂قڐw�c�l�@�݂̂ɂ���j
		double singleScoreWeight = Math.min( 0.01, 0.5 - args.agi.latestGameInfo.getDay() * 0.1 );
		double teamScoreWeight = Math.max( 0.99, 0.5 + args.agi.latestGameInfo.getDay() * 0.1 );

		// �e�G�[�W�F���g�� �P��/�w�c�ő� �� �T �X�R�A�ɉ����ėv����ς���
		for( int i = 1; i <= args.agi.gameSetting.getPlayerNum(); i++ ){

			// �P�̘T�p�^�[�����擾
			InspectedWolfsidePattern singleWolfPattern = args.aguess.getSingleWolfPattern(i);
			// �T�Ƃ��čł��Ó��ȃp�^�[�����擾
			InspectedWolfsidePattern mostWolfPattern = args.aguess.getMostValidWolfPattern(i);

			if( mostWolfPattern != null ){
				// �T�̉\��������
				workReq = new Request( i );
				workReq.vote = 1 / Math.pow(singleWolfPattern.score, singleScoreWeight) * Math.pow(mostWolfPattern.score, teamScoreWeight) * 100;

				// �s���v���̓o�^
				Requests.add(workReq);
			}

		}

		return Requests;

	}

}
