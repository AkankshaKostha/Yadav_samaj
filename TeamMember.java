	package com.example.yadavsajam.model;
	
	import jakarta.persistence.*;
	import lombok.AllArgsConstructor;
	import lombok.Getter;
	import lombok.NoArgsConstructor;
	import lombok.Setter;
	
	@Entity
	@Table(name = "team_members")
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public class TeamMember {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	    @Column(name = "full_name")
	    private String fullName;
	
	    @Column(name = "role")
	    private String role;
	
	    @Column(name = "email")
	    private String email;
	
	    @Column(name = "phone")
	    private String phone;
	
	    @Column(name = "about", length = 2000)
	    private String about;
	
	    @Column(name = "photo_file_name")
	    private String photoFileName;
	
	    @Column(name = "active")
	    private Boolean active = true;
	
	    @Column(name = "age")
	    private Integer age = 0;
	
	    @Column(name = "city")
	    private String city;
	
	    @Column(name = "designation")
	    private String designation;
	}
