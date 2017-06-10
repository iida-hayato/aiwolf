package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.condition.AndCondition;
import jp.iida.hayato.aiwolf.condition.OrCondition;
import jp.iida.hayato.aiwolf.condition.RoleCondition;
import jp.iida.hayato.aiwolf.lib.ComingOut;
import org.aiwolf.common.data.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * �����u��{�w�`�ρv�N���X
 */
public final class Formation_Basic extends AbstractGuessStrategy {

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {

		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();

		// ���b�n�����ꂼ��ŏ��ɍs��ꂽ�������߂�
		int firstSeerCODay = Integer.MAX_VALUE;
		int firstMediumCODay = Integer.MAX_VALUE;
		for( ComingOut co : args.agi.comingOutList ){
			if( co.role == Role.SEER ){
				firstSeerCODay = Math.min(co.commingOutTalk.getDay(), firstSeerCODay);
			}
			if( co.role == Role.MEDIUM ){
				firstMediumCODay = Math.min(co.commingOutTalk.getDay(), firstMediumCODay);
			}
		}

		// ��S�U�͂��܂茩�Ȃ�
		List<Integer> seers = args.agi.getEnableCOAgentNo(Role.SEER);
		if( !seers.isEmpty() ){
			//TODO ���Ґ��Ή�
			final double allFakeCorrelation[] = { 0.1, 0.1, 0.3, 0.4, 0.6, 0.7, 0.8, 1.0, 1.1, 1.1, 1.2, 1.2, 1.3, 1.3, 1.4, 1.4, 1.5, 1.5 };

			AndCondition allFakeCondition = new AndCondition();

			// �e�삪�Tor���ł���������쐬���A�S�U�����ɒǉ�����
			for( int seer : seers ){
				RoleCondition wolfCondition = RoleCondition.getRoleCondition( seer, Role.WEREWOLF );
				RoleCondition posCondition = RoleCondition.getRoleCondition( seer, Role.POSSESSED );

				OrCondition orCondition = new OrCondition();
				orCondition.addCondition(wolfCondition).addCondition(posCondition);

				allFakeCondition.addCondition(orCondition);
			}

			Guess guess = new Guess();
			guess.condition = allFakeCondition;
			guess.correlation = allFakeCorrelation[firstSeerCODay];
			guesses.add(guess);
		}


		// ��S�U�͂��܂茩�Ȃ�
		List<Integer> mediums = args.agi.getEnableCOAgentNo(Role.MEDIUM);
		if( !mediums.isEmpty() ){
			//TODO ���Ґ��Ή�
			final double allFakeCorrelation[] = { 0.1, 0.1, 0.4, 0.7, 1.0, 1.0, 1.1, 1.1, 1.2, 1.2, 1.3, 1.3, 1.4, 1.4, 1.5, 1.5, 1.6, 1.6 };

			AndCondition allFakeCondition = new AndCondition();

			// �e�삪�Tor���ł���������쐬���A�S�U�����ɒǉ�����
			for( int medium : mediums ){
				RoleCondition wolfCondition = RoleCondition.getRoleCondition( medium, Role.WEREWOLF );
				RoleCondition posCondition = RoleCondition.getRoleCondition( medium, Role.POSSESSED );

				OrCondition orCondition = new OrCondition();
				orCondition.addCondition(wolfCondition).addCondition(posCondition);

				allFakeCondition.addCondition(orCondition);
			}

			Guess guess = new Guess();
			guess.condition = allFakeCondition;
			guess.correlation = allFakeCorrelation[firstMediumCODay];
			guesses.add(guess);
		}


//		// ��T�͂��܂茩�Ȃ��H
//		if( !mediums.isEmpty() ){
//			for( int medium : mediums ){
//				Guess guess = new Guess();
//				guess.condition = new RoleCondition( medium, Role.WEREWOLF );
//				guess.correlation = 0.95;
//				guesses.add(guess);
//			}
//		}


		// ��썇�킹�ċU���Q�l�ȏ�̏ꍇ�A���l���x���Ă���ƌ���
		if( seers.size() >= 3 || mediums.size() >= 3 || seers.size() + mediums.size() >= 4 ){

			OrCondition orCondition = new OrCondition();
			for( int seer : seers ){
				orCondition.addCondition( RoleCondition.getRoleCondition( seer, Role.POSSESSED ) );
			}
			for( int medium : mediums ){
				orCondition.addCondition( RoleCondition.getRoleCondition( medium, Role.POSSESSED ) );
			}

			Guess guess = new Guess();
			guess.condition = orCondition;
			guess.correlation = 1.8;
			guesses.add(guess);
		}


		// ����͂��܂茩�Ȃ�
		List<Integer> bodyguards = args.agi.getEnableCOAgentNo(Role.BODYGUARD);
		if( !bodyguards.isEmpty() ){
			for( int bodyguard : bodyguards ){
				Guess guess = new Guess();
				guess.condition = RoleCondition.getRoleCondition( bodyguard, Role.POSSESSED );
				guess.correlation = 0.8;
				guesses.add(guess);

				// ���g����Ȃ�A�U�͘T�Ō���
				if( bodyguard != args.agi.latestGameInfo.getAgent().getAgentIdx() ){
					guess = new Guess();
					guess.condition = RoleCondition.getRoleCondition( bodyguard, Role.WEREWOLF );
					guess.correlation = 1.2;
					guesses.add(guess);
				}
			}
		}



		// �������X�g��Ԃ�
		return guesses;
	}

}
