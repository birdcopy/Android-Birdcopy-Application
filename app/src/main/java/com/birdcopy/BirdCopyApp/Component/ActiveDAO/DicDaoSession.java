package com.birdcopy.BirdCopyApp.Component.ActiveDAO;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DicDaoSession extends AbstractDaoSession {

    private final DaoConfig bE_DIC_PUBDaoConfig;

    private final BE_DIC_PUBDao bE_DIC_PUBDao;

    public DicDaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bE_DIC_PUBDaoConfig = daoConfigMap.get(BE_DIC_PUBDao.class).clone();
        bE_DIC_PUBDaoConfig.initIdentityScope(type);

        bE_DIC_PUBDao = new BE_DIC_PUBDao(bE_DIC_PUBDaoConfig, this);

        registerDao(BE_DIC_PUB.class, bE_DIC_PUBDao);
    }
    
    public void clear() {
        bE_DIC_PUBDaoConfig.getIdentityScope().clear();
    }

    public BE_DIC_PUBDao getBE_DIC_PUBDao() {
        return bE_DIC_PUBDao;
    }

}