package com.lolsearcher.persistance.failmatchids.model.entity.match;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.ParticipantDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "MEMBERS",indexes = @Index(columnList = "summonerId"))
public class Member {
	
	@EmbeddedId
	private MemberCompKey ck;

	@JsonBackReference
	@MapsId("matchId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MATCH_ID", referencedColumnName = "ID")
	private Match match;

	@JsonManagedReference
	@OneToOne(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Perks perks;

	private Short assists;
	private Byte baronKills;
	private Byte bountyLevel;
	private int champExperience;
	private Byte champLevel;
	private Short championId;
	private String championName;
	private int championTransform;
	private int consumablesPurchased;
	private int damageDealtToBuildings;
	private int damageDealtToObjectives;
	private int damageDealtToTurrets;
	private int damageSelfMitigated;
	private Short deaths;
	private Short detectorWardsPlaced;
	private Short doubleKills;
	private Byte dragonKills;
	private boolean firstBloodAssist;
	private boolean firstBloodKill;
	private boolean firstTowerAssist;
	private boolean firstTowerKill;
	private boolean gameEndedInEarlySurrender;
	private boolean gameEndedInSurrender;
	private int goldEarned;
	private int goldSpent;
	private String individualPosition;
	private Byte inhibitorTakedowns;
	private Byte inhibitorsLost;
	private Short item0;
	private Short item1;
	private Short item2;
	private Short item3;
	private Short item4;
	private Short item5;
	private Short item6;
	private Short itemsPurchased;
	private int killingSprees;
	private Short kills;
	private String lane;
	private int largestCriticalStrike;
	private int largestKillingSpree;
	private int largestMultiKill;
	private int longestTimeSpentLiving;
	private int magicDamageDealt;
	private int magicDamageDealtToChampions;
	private int magicDamageTaken;
	private int neutralMinionsKilled;
	private Byte nexusKills;
	private Byte nexusTakedowns;
	private Byte nexusLost;
	private int objectivesStolen;
	private int objectivesStolenAssists;
	private int participantId;
	private Short pentaKills;
	private int physicalDamageDealt;
	private int physicalDamageDealtToChampions;
	private int physicalDamageTaken;
	private int profileIcon;
	private String puuid;
	private Short quadraKills;
	private String riotIdName;
	private String riotIdTagline;
	private String role;
	private Short sightWardsBoughtInGame;
	private Short spell1Casts;
	private Short spell2Casts;
	private Short spell3Casts;
	private Short spell4Casts;
	private Short summoner1Casts;
	private Short summoner1Id;
	private Short summoner2Casts;
	private Short summoner2Id;
	private String summonerId;
	private int summonerLevel;
	private String summonerName;
	private boolean teamEarlySurrendered;
	private Short teamId;
	private String teamPosition;
	private int timeCCingOthers;
	private int timePlayed;
	private int totalDamageDealt;
	private int totalDamageDealtToChampions;
	private int totalDamageShieldedOnTeammates;
	private int totalDamageTaken;
	private int totalHeal;
	private int totalHealsOnTeammates;
	private int totalMinionsKilled;
	private int totalTimeCCDealt;
	private int totalTimeSpentDead;
	private int totalUnitsHealed;
	private Short tripleKills;
	private int trueDamageDealt;
	private int trueDamageDealtToChampions;
	private int trueDamageTaken;
	private Byte turretKills;
	private Byte turretTakedowns;
	private Byte turretsLost;
	private Short unrealKills;
	private Short visionScore;
	private Short visionWardsBoughtInGame;
	private Short wardsKilled;
	private Short wardsPlaced;
	private boolean win;

	public Member(ParticipantDto participantDto) {
		this.assists = participantDto.getAssists();
		this.baronKills = participantDto.getBaronKills();
		this.bountyLevel = participantDto.getBountyLevel();
		this.champExperience = participantDto.getChampExperience();
		this.champLevel = participantDto.getChampLevel();
		this.championId = participantDto.getChampionId();
		this.championName = participantDto.getChampionName();
		this.championTransform = participantDto.getChampionTransform();
		this.consumablesPurchased = participantDto.getConsumablesPurchased();
		this.damageDealtToBuildings = participantDto.getDamageDealtToBuildings();
		this.damageDealtToObjectives = participantDto.getDamageDealtToObjectives();
		this.damageDealtToTurrets = participantDto.getDamageDealtToTurrets();
		this.damageSelfMitigated = participantDto.getDamageSelfMitigated();
		this.deaths = participantDto.getDeaths();
		this.detectorWardsPlaced = participantDto.getDetectorWardsPlaced();
		this.doubleKills = participantDto.getDoubleKills();
		this.dragonKills = participantDto.getDragonKills();
		this.firstBloodAssist = participantDto.isFirstBloodAssist();
		this.firstBloodKill = participantDto.isFirstBloodKill();
		this.firstTowerAssist = participantDto.isFirstTowerAssist();
		this.firstTowerKill = participantDto.isFirstTowerKill();
		this.gameEndedInEarlySurrender = participantDto.isGameEndedInEarlySurrender();
		this.gameEndedInSurrender = participantDto.isGameEndedInSurrender();
		this.goldEarned = participantDto.getGoldEarned();
		this.goldSpent = participantDto.getGoldSpent();
		this.individualPosition = participantDto.getIndividualPosition();
		this.inhibitorTakedowns = participantDto.getInhibitorTakedowns();
		this.inhibitorsLost = participantDto.getInhibitorsLost();
		this.item0 = participantDto.getItem0();
		this.item1 = participantDto.getItem1();
		this.item2 = participantDto.getItem2();
		this.item3 = participantDto.getItem3();
		this.item4 = participantDto.getItem4();
		this.item5 = participantDto.getItem5();
		this.item6 = participantDto.getItem6();
		this.itemsPurchased = participantDto.getItemsPurchased();
		this.killingSprees = participantDto.getKillingSprees();
		this.kills = participantDto.getKills();
		this.lane = participantDto.getLane();
		this.largestCriticalStrike = participantDto.getLargestCriticalStrike();
		this.largestKillingSpree = participantDto.getLargestKillingSpree();
		this.largestMultiKill = participantDto.getLargestMultiKill();
		this.longestTimeSpentLiving = participantDto.getLongestTimeSpentLiving();
		this.magicDamageDealt = participantDto.getMagicDamageDealt();
		this.magicDamageDealtToChampions = participantDto.getMagicDamageDealtToChampions();
		this.magicDamageTaken = participantDto.getMagicDamageTaken();
		this.neutralMinionsKilled = participantDto.getNeutralMinionsKilled();
		this.nexusKills = participantDto.getNexusKills();
		this.nexusTakedowns = participantDto.getNexusTakedowns();
		this.nexusLost = participantDto.getNexusLost();
		this.objectivesStolen = participantDto.getObjectivesStolen();
		this.objectivesStolenAssists = participantDto.getObjectivesStolenAssists();
		this.participantId = participantDto.getParticipantId();
		this.pentaKills = participantDto.getPentaKills();
		this.physicalDamageDealt = participantDto.getPhysicalDamageDealt();
		this.physicalDamageDealtToChampions = participantDto.getPhysicalDamageDealtToChampions();
		this.physicalDamageTaken = participantDto.getPhysicalDamageTaken();
		this.profileIcon = participantDto.getProfileIcon();
		this.puuid = participantDto.getPuuid();
		this.quadraKills = participantDto.getQuadraKills();
		this.riotIdName = participantDto.getRiotIdName();
		this.riotIdTagline = participantDto.getRiotIdTagline();
		this.role = participantDto.getRole();
		this.sightWardsBoughtInGame = participantDto.getSightWardsBoughtInGame();
		this.spell1Casts = participantDto.getSpell1Casts();
		this.spell2Casts = participantDto.getSpell2Casts();
		this.spell3Casts = participantDto.getSpell3Casts();
		this.spell4Casts = participantDto.getSpell4Casts();
		this.summoner1Casts = participantDto.getSummoner1Casts();
		this.summoner1Id = participantDto.getSummoner1Id();
		this.summoner2Casts = participantDto.getSummoner2Casts();
		this.summoner2Id = participantDto.getSummoner2Id();
		this.summonerId = participantDto.getSummonerId();
		this.summonerLevel = participantDto.getSummonerLevel();
		this.summonerName = participantDto.getSummonerName();
		this.teamEarlySurrendered = participantDto.isTeamEarlySurrendered();
		this.teamId = participantDto.getTeamId();
		this.teamPosition = participantDto.getTeamPosition();
		this.timeCCingOthers = participantDto.getTimeCCingOthers();
		this.timePlayed = participantDto.getTimePlayed();
		this.totalDamageDealt = participantDto.getTotalDamageDealt();
		this.totalDamageDealtToChampions = participantDto.getTotalDamageDealtToChampions();
		this.totalDamageShieldedOnTeammates = participantDto.getTotalDamageShieldedOnTeammates();
		this.totalDamageTaken = participantDto.getTotalDamageTaken();
		this.totalHeal = participantDto.getTotalHeal();
		this.totalHealsOnTeammates = participantDto.getTotalHealsOnTeammates();
		this.totalMinionsKilled = participantDto.getTotalMinionsKilled();
		this.totalTimeCCDealt = participantDto.getTotalTimeCCDealt();
		this.totalTimeSpentDead = participantDto.getTotalTimeSpentDead();
		this.totalUnitsHealed =participantDto.getTotalUnitsHealed();
		this.tripleKills = participantDto.getTripleKills();
		this.trueDamageDealt = participantDto.getTrueDamageDealt();
		this.trueDamageDealtToChampions = participantDto.getTrueDamageDealtToChampions();
		this.trueDamageTaken = participantDto.getTrueDamageTaken();
		this.turretKills = participantDto.getTurretKills();
		this.turretTakedowns = participantDto.getTurretTakedowns();
		this.turretsLost = participantDto.getTurretsLost();
		this.unrealKills = participantDto.getUnrealKills();
		this.visionScore = participantDto.getVisionScore();
		this.visionWardsBoughtInGame = participantDto.getVisionWardsBoughtInGame();
		this.wardsKilled = participantDto.getWardsKilled();
		this.wardsPlaced = participantDto.getWardsPlaced();
		this.win = participantDto.isWin();
	}

	public void removeMatch() {
		if(match!=null) {
			match.removeMember(this);
			match = null;
		}
	}

	public void setMatch(Match match) {
		if(this.match!=null) {
			this.match.removeMember(this);
		}
		this.match = match;
		this.match.addMember(this);
	}
}
