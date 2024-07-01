package com.adc.json;

import com.adc.dao.ProvinceDao;
import com.adc.utils.SessionManager;
import org.apache.ibatis.session.SqlSession;

public abstract class AbstractParser implements Parser {
    private SqlSession session;

    protected SqlSession getSession() {
        if (session == null) {
            session = SessionManager.get();
        }
        return session;
    }


    @Override
    public void parser() {
        SqlSession session = getSession();
        doParser(session);
        closeSession(session);
    }

    protected abstract void doParser(SqlSession session);

    private void closeSession(SqlSession session) {
        session.close();
    }
}
