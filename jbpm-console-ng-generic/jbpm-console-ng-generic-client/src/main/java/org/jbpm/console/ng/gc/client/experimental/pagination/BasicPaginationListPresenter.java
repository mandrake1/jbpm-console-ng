/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.console.ng.gc.client.experimental.pagination;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jbpm.console.ng.gc.client.list.base.BasePresenter;
import org.jbpm.console.ng.ht.model.events.SearchEvent;
import org.kie.workbench.common.widgets.client.search.ClearSearchEvent;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;
import org.uberfire.workbench.model.menu.Menus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.ListDataProvider;
import java.util.ArrayList;
import java.util.List;
import org.jbpm.console.ng.gc.client.i18n.Constants;
import org.jbpm.console.ng.gc.client.util.DataGridUtils;

@Dependent
@WorkbenchScreen(identifier = "Pagination For Tables")
public class BasicPaginationListPresenter extends BasePresenter<DataMockSummary, BasicPagiantionListViewImpl> implements GenericDataProvider<DataMockSummary> {

   
    private List<DataMockSummary> serverSideData  = new ArrayList<DataMockSummary>(1000);

    public interface BasicPaginationListView extends UberView<BasicPaginationListPresenter> {

        void showBusyIndicator(String message);

        void hideBusyIndicator();

    }

    @WorkbenchMenu
    public Menus getMenus() {
        return menus;
    }

    @WorkbenchPartView
    public UberView<BasicPaginationListPresenter> getView() {
        return view;
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Pagination For Tables";
    }

    @Inject
    private Event<ClearSearchEvent> clearSearchEvent;

    private Constants constants = GWT.create(Constants.class);

    @PostConstruct
    public void init() {
        super.NEW_ITEM_MENU = "Create Data";
        super.makeMenuBar();
    }

    public void deleteColumn(final String id) {
        view.displayNotification("Removing Column " + id);
    }

    @Override
    public List<DataMockSummary> getAllServerSideData() {
        return allItemsSummaries;
    }

    @Override
    public ListDataProvider<DataMockSummary> getClientSideDataProvider() {
        return dataProvider;
    }

    @Override
    public void refresh(int start, int offset, boolean clear) {
        if(serverSideData.size() > 0 && serverSideData.size() + 1 > (start + offset)){
            view.displayNotification(" Getting from the server from (" + start +" ) to (" + (start + offset) +")");
            allItemsSummaries = serverSideData.subList(start, start + offset);
            view.setCurrentFilter("");
            filterItems(view.getCurrentFilter(), view.getListGrid(), clear);
            clearSearchEvent.fire(new ClearSearchEvent());
            view.setCurrentFilter("");
        }
    }
    
    @Override
    protected void refreshItems() {
        refresh(view.getPager().getCurrentPage() * DataGridUtils.pageSize, (DataGridUtils.pageSize * DataGridUtils.clientSidePages), true);
    }
    
    @Override
    protected void onSearchEvent(SearchEvent searchEvent) {
        view.setCurrentFilter(searchEvent.getFilter());

    }

    @Override
    protected void createItem() {
        for (int i = 0; i < 1000; i++) {
            serverSideData.add(new DataMockSummary("ID:" + i, "Data 1:" + i, "Data 2:" + i, "Data 3:" + i, "Data 4:" + i));
        }
        view.displayNotification(" 1000 Items created in the backend !");
    }

    @Override
    protected void readItem(Long id) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void updateItem(Long id) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void deleteItem(Long id) {
        // TODO Auto-generated method stub
    }

}
