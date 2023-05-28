package de.esnecca.multi.db;

import java.math.BigInteger;

public class DbEntry {

    private BigInteger bi;
    private int gameid;
    private int result;
    private int inserted;

    public DbEntry(BigInteger bi, int gameid, int result, int inserted) {
        this.bi = bi;
        this.gameid = gameid;
        this.result = result;
        this.inserted = inserted;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DbEntry other = (DbEntry) obj;
        if (bi == null) {
            if (other.bi != null)
                return false;
        } else if (!bi.equals(other.bi))
            return false;
        if (gameid != other.gameid)
            return false;
        if (result != other.result)
            return false;
        if (inserted != other.inserted)
            return false;
        return true;
    }

    public BigInteger getBi() {
        return bi;
    }

    public int getGameid() {
        return gameid;
    }

    public int getResult() {
        return result;
    }

    public int getInserted() {
        return inserted;
    }
}
