package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.condition.OrCondition;
import jp.iida.hayato.aiwolf.condition.RoleCondition;
import org.aiwolf.common.data.Role;

import java.util.ArrayList;

/**
 * �����u����ہv�N���X
 * ����X�R�A�̉���Ə��Ղ̏󋵍�肪��ړI
 */
public final class FirstImpression extends AbstractGuessStrategy {

	// �������X�g
	private ArrayList<Guess> guesses = null;

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {

		// ����擾���ɍs�����N�G�X�g�̐ݒ���s��
		if( guesses == null ){
			guesses = new ArrayList<Guess>();
			for( int i = 1; i <= args.agi.gameSetting.getPlayerNum(); i++ ){
				Guess guess;

				// �e�G�[�W�F���g���Tor���l�̃p�^�[�����A�����_����0.95�`1.05�{�Ɛ�������
				RoleCondition wolfCondition = RoleCondition.getRoleCondition( i, Role.WEREWOLF );
				RoleCondition posCondition = RoleCondition.getRoleCondition( i, Role.POSSESSED );

				guess = new Guess();
				guess.condition = new OrCondition().addCondition(wolfCondition).addCondition(posCondition);
				guess.correlation = 0.95 + Math.random() * 0.1;
				guesses.add(guess);

			}
		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
