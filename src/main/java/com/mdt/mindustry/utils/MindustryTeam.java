package com.mdt.mindustry.utils;

import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.game.Teams;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MindustryTeam {

    public Seq<Teams.TeamData> getAvailableTeams() {
        return Vars.state.teams.getActive().select(t -> t.hasCore() && t.team != Team.derelict);
    }

}
