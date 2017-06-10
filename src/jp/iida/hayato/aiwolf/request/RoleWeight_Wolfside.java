package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.lib.CauseOfDeath;
import jp.iida.hayato.aiwolf.lib.Judge;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * �s����p�u��E�̏d�݁v
 */
public final class RoleWeight_Wolfside extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		//TODO ��]�T�ȂǁA�󋵔��f���@�艺����i�S�̓I�Ɂj
		//TODO ���Ґ��Ή�

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;



		// ��E�����܂�Ă��邩�`�F�b�N
		boolean isAttackedSeer = false;
		boolean isAttackedMedium = false;
		for( int i = 1; i <= args.agi.gameSetting.getPlayerNum(); i++  ){
			if( args.agi.agentState[i].causeofDeath == CauseOfDeath.ATTACKED ){
				if( args.agi.agentState[i].comingOutRole == Role.SEER ){
					isAttackedSeer = true;
				}
				if( args.agi.agentState[i].comingOutRole == Role.MEDIUM ){
					isAttackedMedium = true;
				}
			}
		}


		// ���Ⴂ�E���Ⴂ
		List<Judge> seerJudges = args.agi.getSeerJudgeList();
		for( Judge judge : seerJudges ){

			// �����Ȕ���ł���΃X�L�b�v
			if( !judge.isEnable() ){
				continue;
			}

			// �픻��҂����Ɏ���ł���΃X�L�b�v
			if( args.agi.agentState[judge.targetAgentNo].causeofDeath != CauseOfDeath.ALIVE ){
				continue;
			}

			if( judge.result == Species.WEREWOLF){

				// ���Ⴂ
				workReq = new Request( judge.targetAgentNo );

				// �m��l�O�łȂ���
				if( !args.agi.selfViewInfo.isFixWolfSide(judge.agentNo) ){
					workReq.inspect = Math.min( 0.2 + gameInfo.getDay() * 0.15 , 0.8 );	// ���̌o�߂ɏ]�� 0.35(1d)��0.50�c0.80(4d) ��0.80�܂ŏ㏸
					workReq.guard = Math.min( 0.1 + gameInfo.getDay() * 0.02 , 0.8 );	// ���̌o�߂ɏ]�� 0.12(1d)��0.14�c0.20(4d) ��0.20�܂ŏ㏸
					workReq.vote = Math.max( 3.8 - gameInfo.getDay() * 0.7 , 1.0 );		// ���̌o�߂ɏ]�� 3.10(1d)��2.40�c1.00(4d) ��1.00�܂Œቺ
				}

				workReq.attack = 0.1;

				// �s���v���̓o�^
				Requests.add(workReq);

			}else{

				// ���Ⴂ
				workReq = new Request( judge.targetAgentNo );

				workReq.inspect = Math.min( 0.6 + gameInfo.getDay() * 0.1 , 1.0 );	// ���̌o�߂ɏ]�� 0.70(1d)��0.80�c1.00(4d) ��1.00�܂ŏ㏸
				workReq.guard = 1.2;
				workReq.vote = Math.min( 0.4 + gameInfo.getDay() * 0.2 , 1.0 );		// ���̌o�߂ɏ]�� 0.60(1d)��0.80�c1.00(3d) ��1.00�܂ŏ㏸�@���[���킹�\�͂��ア�̂ŁA���߂Ɏ��ł�

				// �^������̔��肩
				if( !args.agi.isWolf(judge.agentNo) ){
					workReq.attack = 1.2;
				}

				// �s���v���̓o�^
				Requests.add(workReq);

			}
		}

		// �e��E��CO�҂��擾
		List<Integer> seers = args.agi.getEnableCOAgentNo(Role.SEER);
		List<Integer> mediums = args.agi.getEnableCOAgentNo(Role.MEDIUM);
		List<Integer> bodyguards = args.agi.getEnableCOAgentNo(Role.BODYGUARD);
		List<Integer> villagers = args.agi.getEnableCOAgentNo(Role.VILLAGER);

		// ��CO��
		for( int seer : seers ){
			workReq = new Request( seer );

			// �΍R�肢�͔�����
			workReq.inspect = 0.05;
			// CO���ɉ����Č�q�𑝂₷
			if( seers.size() <= 1 ){
				// 1CO
				workReq.guard = 10.0;
			}else if( seers.size() <= 2 ){
				// 2CO
				workReq.guard = 4.0;
			}else if( seers.size() <= 3 ){
				// 3CO
				workReq.guard = 2.0;
			}else{
				// 4CO�ȏ�
				workReq.guard = 1.0;
			}
			// �΍R�肪���܂�Ă��āA�P�삪�����Ă���Ό�q�𔖂�����
			if( isAttackedSeer &&
			    mediums.size() == 1 && !isAttackedMedium ){
				workReq.guard *= 0.7;
			}

			workReq.vote = Math.min(Math.max( -0.2 + gameInfo.getDay() * 0.3 , 0.0) , 1.0 ) + (isAttackedSeer ? 0.2 : 0.0);	// ���̌o�߂ɏ]�� 0.10(1d)��0.70�c1.00(4d) ��1.00�܂ŏ㏸
			workReq.attack = 2.0;

			// �s���v���̓o�^
			Requests.add(workReq);
		}

		// ��CO��
		for( int medium : mediums ){
			workReq = new Request(medium);

			// ��\�肢�͔�����
			workReq.inspect = 0.05;
			// �P��͉��l���グ�A����͉��l��������
			if( mediums.size() <= 1 ){
				// 1CO
				workReq.guard = 3.0;
				workReq.vote = Math.min(Math.max( -0.2 + gameInfo.getDay() * 0.3 , 0.0) , 1.0 );	// ���̌o�߂ɏ]�� 0.10(1d)��0.70�c1.00(4d) ��1.00�܂ŏ㏸
				workReq.attack = 0.4;
			}else{
				// 2CO�ȏ�
				workReq.guard = 0.1;
				workReq.vote = Math.max( 1.0 + mediums.size() + (isAttackedMedium ? 0.6 : 0.0) - gameInfo.getDay() * 0.5 , 1.0 );	// ���̌o�߂ɏ]�� 2.50(1d)��2.00�c1.0(4d) ��1.00�܂Œቺ(2CO���BCO�����ɍX��+1.0)
				workReq.attack = 0.05;
			}

			// �s���v���̓o�^
			Requests.add(workReq);
		}


		// ��CO��
		for( int bodyguard : bodyguards ){
			workReq = new Request(bodyguard);

			workReq.inspect = 0.3;	// ���݂�҂��Ȃ��琄���Ō��ߑł̂��L��
			workReq.guard = 0.001;	// �뎋�_�ł͎����ȊO�͋U��m��
			workReq.vote = 1.0;
			workReq.attack = 16.0;

			// �s���v���̓o�^
			Requests.add(workReq);
		}


		// ��CO��
		for( int villager : villagers ){
			workReq = new Request(villager);

			// ���m��̂��߉��l��������
			workReq.guard = 0.8;
			workReq.vote = 1.0;
			workReq.attack = 0.8;

			// �s���v���̓o�^
			Requests.add(workReq);
		}


		return Requests;

	}

}
