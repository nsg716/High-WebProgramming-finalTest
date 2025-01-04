/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.model.dao;

import java.util.List;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Model;

/**
 * ModelDAO
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
public interface ModelDAO {

	public Model getModelById(String modelId);
 
	
	public Model getModel(Model model);
	
	public Model createModel(Model model);
    
	public Model updateModel(Model model);
    
	public void deleteModel(Model model);
	
	public List<Model> getModels(Model model);
    
	public List<Model> getModelsByAdminId(String ID);

	// 아이디 중복 확인 메서드
    public boolean isModelIdDuplicate(String modelId);
    
    
	public List<Model> filterModels(String ID, String name, String version, String type);

    
    // 전체 사용자 호출 
    public List<Model> getAllModels();
}

