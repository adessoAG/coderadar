package io.reflectoring.coderadar.graph.useradministration;

import io.reflectoring.coderadar.domain.Team;
import io.reflectoring.coderadar.graph.Mapper;
import io.reflectoring.coderadar.graph.useradministration.domain.TeamEntity;

public class TeamMapper implements Mapper<Team, TeamEntity> {

  private final UserMapper userMapper = new UserMapper();

  @Override
  public Team mapGraphObject(TeamEntity nodeEntity) {
    Team team = new Team();
    team.setName(nodeEntity.getName());
    team.setMembers(userMapper.mapNodeEntities(nodeEntity.getMembers()));
    team.setId(nodeEntity.getId());
    return team;
  }

  @Override
  public TeamEntity mapDomainObject(Team domainObject) {
    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setName(domainObject.getName());
    teamEntity.setMembers(userMapper.mapDomainObjects(domainObject.getMembers()));
    return teamEntity;
  }
}
