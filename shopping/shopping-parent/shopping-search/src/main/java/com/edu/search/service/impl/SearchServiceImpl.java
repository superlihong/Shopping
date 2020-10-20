package com.edu.search.service.impl;

import com.edu.common.bean.SearchResult;
import com.edu.search.dao.SearchDao;
import com.edu.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;
    @Override
    public SearchResult query(String queryString, int page, int rows) throws Exception{
        SolrQuery query=new SolrQuery();
        query.setQuery(queryString);
        query.setStart(rows*(page-1));//起始索引
        query.setRows(rows);
        query.set("df","item_keywords");//从哪个域中检索
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePost("</em>");
        query.setHighlightSimplePre("<em style=\"color:red;\">");

        SearchResult searchResult=searchDao.query(query);
        searchResult.setCurrentPage(page);
        long pageCount=searchResult.getRowCount() % rows==0 ? searchResult.getRowCount() / rows:(searchResult.getRowCount() / rows +1);
        searchResult.setPageCount((int)pageCount);
        return searchResult;
    }
}
