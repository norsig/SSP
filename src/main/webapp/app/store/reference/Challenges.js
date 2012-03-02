Ext.define('Ssp.store.reference.Challenges', {
    extend: 'Ext.data.Store',
    model: 'Ssp.model.reference.ChallengeTO',
    storeId: 'challengesReferenceStore',
	autoLoad: true,
	autoSync: true,

    proxy: {
		type: 'rest',
		url: '/ssp/api/reference/challenge/',
		/*
		api: {
			read: 'data/reference/challenges.json'
		},
		*/
		actionMethods: {create: "POST", 
			read: "GET", 
			update: "PUT", 
			destroy: "DELETE"},
		reader: {
			type: 'json'
		},
        writer: {
            type: 'json'
        }
	}
	
});