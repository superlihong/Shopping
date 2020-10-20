package com.edu.search.dao.impl;

import com.edu.common.bean.Item;
import com.edu.common.bean.SearchResult;
import com.edu.search.dao.SearchDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchDaoImpl implements SearchDao {
    @Autowired
    private SolrServer solrServer;
    @Override
    public SearchResult query(SolrQuery query) throws Exception{
        SearchResult result=new SearchResult();
        QueryResponse response=solrServer.query(query);
        SolrDocumentList list=response.getResults();//所有的数据
        result.setRowCount(list.getNumFound());
        List<Item> items=new ArrayList<>();
        Map<String,Map<String, List<String>>> highlighting=response.getHighlighting();
        for(SolrDocument document:list){
            Item item=new Item();
            item.setId( (String) document.get("id"));
            Map<String, List<String>> listMap=highlighting.get((String)document.get("id"));
            List<String> titles=listMap.get("item_title");
            String title=null;
            if(null != titles && titles.size()>0){
                title=titles.get(0);
            }else {
                title=(String) document.get("item_title");
            }
            item.setTitle(title);
            item.setSell_point((String)document.get("item_sell_point"));
            item.setPrice((Long) document.get("item_price"));
            item.setImage((String) document.get("item_image"));
            item.setCategory_name((String) document.get("item_category_name"));
            items.add(item);
        }
        result.setItem(items);
        return result;
    }
}
