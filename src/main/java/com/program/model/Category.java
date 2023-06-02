package com.program.model;

import com.fasterxml.jackson.annotation.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="category_id")
	private Integer categoryId;

	@Column(unique = true)
	private String categoryName;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
	private List<Status> statuses =new ArrayList<>();

	@JsonProperty("category_id")
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	@JsonProperty("category_name")
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@JsonProperty("statuses")
	public List<Status> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<Status> statuses) {
		this.statuses = statuses;
	}

	public Category(Integer categoryId, String categoryName, List<Status> statuses) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.statuses = statuses;
	}

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryName=" + categoryName + ", statuses=" + statuses + "]";
	}

}
