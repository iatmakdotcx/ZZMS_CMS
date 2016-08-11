package handling.world.exped;

public enum ExpeditionType {

    Balrog(6, 2001, 45, 200),
    Zakum(6, 2002, 50, 200),
    Horntail(6, 2003, 80, 200),
    Pink_Bean(6, 2004, 120, 200),
    Chaos_Zakum(6, 2005, 100, 200),
    ChaosHT(6, 2006, 110, 200),
    Von_Leon(6, 2008, 120, 200),
    Cygnus(6, 2009, 170, 200),
    Akyrum(18, 2009, 120, 250),
    Arkarium(6, 2010, 120, 200),
    Hilla(6, 2011, 70, 120),
    Chaos_Pink_Bean(6, 2012, 170, 200),
    CWKPQ(6, 2007, 90, 200),
    ;

    public int maxMembers, maxParty, exped, minLevel, maxLevel;

    private ExpeditionType(int maxMembers, int exped, int minLevel, int maxLevel) {
        this.maxMembers = maxMembers;
        this.exped = exped;
        this.maxParty = (maxMembers / 2) + (maxMembers % 2 > 0 ? 1 : 0);
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public static ExpeditionType getById(int id) {
        for (ExpeditionType pst : ExpeditionType.values()) {
            if (pst.exped == id) {
                return pst;
            }
        }
        return null;
    }
}
