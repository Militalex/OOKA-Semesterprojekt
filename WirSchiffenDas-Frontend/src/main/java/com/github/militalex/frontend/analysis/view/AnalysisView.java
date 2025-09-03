package com.github.militalex.frontend.analysis.view;

import com.github.militalex.frontend.analysis.AnalysisService;
import com.github.militalex.frontend.analysis.AnalysisSession;
import com.github.militalex.frontend.analysis.view.components.ComponentFactory;
import com.github.militalex.frontend.analysis.view.components.EquipmentRow;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Analyse")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
public class AnalysisView extends Composite<VerticalLayout> {

    public AnalysisView(AnalysisService service) {
        AnalysisSession session = service.createSession(this);

        List<EquipmentRow> equipmentRows = ComponentFactory.getEquipmentRows(session.getSessionId(), service, session.getConfigBinder(), session.getStatesBinder());

        getContent().setSpacing(false);
        getContent().add(
                ComponentFactory.getAnalysisHeader(session.getConfigBinder()),
                ComponentFactory.getHorizontalSeparater(8),
                ComponentFactory.getEquipmentRowsHeader(),
                ComponentFactory.getHorizontalSeparater(5),
                equipmentRows.get(0),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(1),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(2),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(3),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(4),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(5),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(6),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(7),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(8),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(9),
                ComponentFactory.getHorizontalSeparater(),
                equipmentRows.get(10),
                ComponentFactory.getHorizontalSeparater(5),
                ComponentFactory.getAnalyseFooter(session.getSessionId(), service)
        );
        session.pushToUi();
        service.pingAlgorithms(session.getSessionId());
    }
}
