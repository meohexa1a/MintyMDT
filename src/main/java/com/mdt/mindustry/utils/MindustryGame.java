package com.mdt.mindustry.utils;

import mindustry.Vars;
import mindustry.game.Team;

import lombok.experimental.UtilityClass;

import org.jetbrains.annotations.Nullable;

@UtilityClass
public class MindustryGame {

    public @Nullable DefaultGameModeGameOverDetail.SurvivalGameOverDetail isSurvivalGameOver() {
        // defeat: default team core destroyed
        if (Vars.state.rules.defaultTeam.core() == null)
            return new DefaultGameModeGameOverDetail.SurvivalGameOverDetail(false);

        // win: wave winning
        if (Vars.state.rules.waves && Vars.state.rules.winWave > 0 &&
            Vars.state.wave >= Vars.state.rules.winWave &&
            Vars.state.enemies == 0 && !Vars.spawner.isSpawning())
            return new DefaultGameModeGameOverDetail.SurvivalGameOverDetail(true);

        return null;
    }

    public @Nullable DefaultGameModeGameOverDetail.AttackGameOverDetail isAttackGameOver() {
        // defeat: default team core destroyed
        if (Vars.state.rules.defaultTeam.core() == null)
            return new DefaultGameModeGameOverDetail.AttackGameOverDetail(false);

        // win: wave winning
        if (Vars.state.rules.waves && Vars.state.rules.winWave > 0 &&
            Vars.state.wave >= Vars.state.rules.winWave &&
            Vars.state.enemies == 0 && !Vars.spawner.isSpawning())
            return new DefaultGameModeGameOverDetail.AttackGameOverDetail(true);

        // win: all enemy cores destroyed (even all team has destroyed)
        if (Vars.state.teams.getActive().count(t ->
            t.isAlive() && t.team != Team.derelict && t.hasCore()) <= 1)
            return new DefaultGameModeGameOverDetail.AttackGameOverDetail(true);

        return null;
    }

    public @Nullable DefaultGameModeGameOverDetail.PvPGameOverDetail isPvPGameOver() {
        var aliveTeams = Vars.state.teams.getActive()
            .select(t -> t.isAlive() && t.team != Team.derelict && t.hasCore());

        // evaluate wining: all other player core destroyed
        if (aliveTeams.size <= 1)
            return new DefaultGameModeGameOverDetail.PvPGameOverDetail(aliveTeams.isEmpty() ? Team.derelict : aliveTeams.first().team);

        return null;
    }

    // !-------------------------------------------------------------------------!

    public sealed interface DefaultGameModeGameOverDetail {

        record SurvivalGameOverDetail(boolean isPlayerWin) implements DefaultGameModeGameOverDetail {

        }

        record PvPGameOverDetail(Team winner) implements DefaultGameModeGameOverDetail {

        }

        record AttackGameOverDetail(boolean isPlayerWin) implements DefaultGameModeGameOverDetail {

        }
    }
}
