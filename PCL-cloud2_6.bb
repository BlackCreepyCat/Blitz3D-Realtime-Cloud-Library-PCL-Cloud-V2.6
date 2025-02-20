Global PCL_Timer

Const pcl_count=200			; change here to get more than 200 Particles per Cloud

Type PCL_Cloud
 Field pivot 				; cloud-center
 Field light 				; light-entity. must Not be a light
 Field camera 				; camera for nearfade and to face for
 Field r_amb#,g_amb#,b_amb#	; Ambient-Light Color (50)
 Field auto					; automatic Light/Shadow Calc. enabled/disabled (0)
 Field akt_part				; internal Var. for automatic Lightning 
 Field anz_part 			; Amount of Particles
 Field point	 			; particle facing camera on/off
 Field random	 			; particle facing camera with random z-angle
 Field speed#	 			; speed to change colors  (.5/sec.)
 Field renderstep			; stepwitdh to darken a particle during UpdateCloud() ( associates with particle-alpha) (10)
 Field groups				; amount of groups for this cloud (1)
 Field part1[pcl_count]		; particle-handle
 Field part2[pcl_count]		; r \
 Field part3[pcl_count]		; g  > Particle-Color
 Field part4[pcl_count]		; b /
 Field part5#[pcl_count]	; Particle-size (1)
 Field part6#[pcl_count]	; radial distance from center ( for later pos.offset operations)
 Field part7#[pcl_count]	; old shadow-value (1)
 Field part8#[pcl_count]	; new shadow value for smooth adjust (1)
 Field part9#[pcl_count]	; particle-alpha (1)
 Field part10#[pcl_count]	; random range-index
 Field part11[pcl_count]	; group-number
 Field part12[pcl_count]	; nearfading on/off
 Field part_n#[pcl_count]	; nearfading near
 Field part_f#[pcl_count]	; nearfading far
 Field partx#[pcl_count]	; x-pos.
 Field party#[pcl_count]	; y-pos.
 Field partz#[pcl_count]	; z-pos.
End Type

Type pcl_groupbrush
 Field pivot
 Field number
 Field near
 Field near_n#
 Field near_f#
 Field autofade
 Field far_n#
 Field far_f#
 Field size_min#,size_max#
 Field size_mode
 Field pos_min#,pos_max#
 Field rot_x,rot_y,rot_z,rand_x,rand_y,rand_z
 Field alpha_min#,alpha_max#
 Field shini_min#,shini_max#
 Field r1,g1,b1 			; Particle-Color-Range (100)
 Field r2,g2,b2 			;    - II -			  (255)
 Field blend				; Particle Blendmode Alpha/Additiv (0)
 Field fx					; particle Fx 
 Field p_type	 			; Entitytype
 Field pick		 			; Pickmode
 Field obscure	 			; obscurer for pickmode
 Field texture$ [7]			; particle-texture-file
 Field texflag [7]			; particle-texture-flag
 Field texmode [7]			; particle-texture-blendmode
 Field texscx# [7]			; particle-texture-scale-x
 Field texscy# [7]			; particle-texture-scale-y
 Field texposx# [7]			; particle-texture-pos-x
 Field texposy# [7]			; particle-texture-pos-y
 Field texrot# [7]			; particle-texture-rotation
End Type

Function PCL_NewCloud(pcl_mesh,pcl_anz,pcl_groups=1)
Local pcl_vertex[3]
Local pcl_dummy2=CreatePivot()
pcl_newcloud.pcl_cloud=New pcl_cloud
pcl_newcloud\pivot=CreatePivot()
For a=1 To pcl_anz
 SeedRnd Rand(10,999999)
 pcl_newcloud\part1[a]=pcl_createflat(pcl_newcloud\pivot)
 s=CountSurfaces(pcl_mesh)
 s=GetSurface(pcl_mesh,Rand(1,s))
 t=Rand(0,CountTriangles(s)-1)
 For d=0 To 2
  pcl_vertex[d]=TriangleVertex(s,t,d)
 Next
 TFormPoint VertexX#(s,pcl_vertex[0]),VertexY#(s,pcl_vertex[0]),VertexZ#(s,pcl_vertex[0]),pcl_mesh,0
 PositionEntity pcl_newcloud\part1[a],TFormedX#(),TFormedY#(),TFormedZ#()
 TFormPoint VertexX#(s,pcl_vertex[1]),VertexY#(s,pcl_vertex[1]),VertexZ#(s,pcl_vertex[1]),pcl_mesh,0
 PositionEntity pcl_dummy2,TFormedX#(),TFormedY#(),TFormedZ#()
 PointEntity pcl_newcloud\part1[a],pcl_dummy2
 MoveEntity pcl_newcloud\part1[a],0,0,Rnd(0,EntityDistance#(pcl_newcloud\part1[a],pcl_dummy2))
 TFormPoint VertexX#(s,pcl_vertex[2]),VertexY#(s,pcl_vertex[2]),VertexZ#(s,pcl_vertex[2]),pcl_mesh,0
 PositionEntity pcl_dummy2,TFormedX#(),TFormedY#(),TFormedZ#()
 PointEntity pcl_newcloud\part1[a],pcl_dummy2
 MoveEntity pcl_newcloud\part1[a],0,0,Rnd(0,EntityDistance#(pcl_newcloud\part1[a],pcl_dummy2))
 pcl_newcloud\part10#[a]=Rnd(0.0,1.0)
 pcl_newcloud\part5#[a]=1 : pcl_newcloud\part6#[a]=EntityDistance#(pcl_newcloud\part1[a],pcl_newcloud\pivot)
 pcl_newcloud\part7#[a]=1 : pcl_newcloud\part8#[a]=1 : pcl_newcloud\part9#[a]=1
 pcl_group=pcl_group+1
 If pcl_group>pcl_groups Then pcl_group=1
 pcl_newcloud\part11[a]=pcl_group
 pcl_newcloud\partx#[a]=EntityX#(pcl_newcloud\part1[a])
 pcl_newcloud\party#[a]=EntityY#(pcl_newcloud\part1[a])
 pcl_newcloud\partz#[a]=EntityZ#(pcl_newcloud\part1[a])
 pcl_sx#=pcl_newcloud\part5#[a]*PCL_EntityScale#(pcl_newcloud\pivot,0)
 pcl_sy#=pcl_newcloud\part5#[a]*PCL_EntityScale#(pcl_newcloud\pivot,1)
 pcl_sz#=pcl_newcloud\part5#[a]*PCL_EntityScale#(pcl_newcloud\pivot,2)
 EntityBox pcl_newcloud\part1[a],pcl_sx#/(-2),pcl_sy#/(-2),pcl_sz#/(-2),pcl_sx#,pcl_sy#,pcl_sz#
 EntityRadius pcl_newcloud\part1[a],(pcl_sx#/2+pcl_sy#/2+pcl_sz#/2)/3.0
Next
FreeEntity pcl_dummy2
For a=1 To pcl_groups
 temp.pcl_groupbrush=New pcl_groupbrush
 temp\pivot=pcl_newcloud\pivot
 temp\number=a
 temp\r1=100 : temp\g1=100 : temp\b1=100
 temp\r2=255 : temp\g2=255 : temp\b2=255
 temp\size_min#=1 : temp\size_max#=1
 temp\pos_min#=1 : temp\pos_max#=1
 temp\rot_x=0 : temp\rot_y=0 : temp\rot_z=0 : temp\rand_x=0 : temp\rand_y=0 : temp\rand_z=0
 temp\alpha_min#=1 : temp\alpha_max#=1
 temp\shini_min#=0 : temp\shini_max#=0
Next


pcl_newcloud\anz_part=pcl_anz : pcl_newcloud\akt_part=1
pcl_newcloud\r_amb=50 : pcl_newcloud\g_amb=50 : pcl_newcloud\b_amb=50
pcl_newcloud\speed=0.50
pcl_newcloud\renderstep=10
pcl_newcloud\groups=pcl_groups
pcl_setparticlecolor(pcl_newcloud\pivot,100,100,100,255,255,255)

PositionEntity pcl_newcloud\pivot,0,0,0
Return pcl_newcloud\pivot
End Function

Function PCL_SaveCloud(pcl_pivot,pcl_save$)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  pcl_f=WriteFile(pcl_save$)
  If pcl_f<>0
   WriteLine pcl_f,pcl_cld\anz_part
   WriteLine pcl_f,pcl_cld\groups
   For a=1 To pcl_cld\anz_part
    WriteLine pcl_f,pcl_cld\partx#[a]
    WriteLine pcl_f,pcl_cld\party#[a]
    WriteLine pcl_f,pcl_cld\partz#[a]
    WriteLine pcl_f,EntityPitch#(pcl_cld\part1[a])
    WriteLine pcl_f,EntityYaw#(pcl_cld\part1[a])
    WriteLine pcl_f,EntityRoll#(pcl_cld\part1[a])
    WriteLine pcl_f,pcl_cld\part11[a]
    WriteLine pcl_f,pcl_cld\part12[a]
    WriteLine pcl_f,pcl_cld\part10#[a]
   Next
   WriteLine pcl_f,pcl_cld\r_amb#
   WriteLine pcl_f,pcl_cld\g_amb#
   WriteLine pcl_f,pcl_cld\b_amb#
   ;WriteLine pcl_f,pcl_cld\autofade
   ;WriteLine pcl_f,pcl_cld\fade_min#
   ;WriteLine pcl_f,pcl_cld\fade_max#
   WriteLine pcl_f,pcl_cld\auto	 
   WriteLine pcl_f,pcl_cld\point
   WriteLine pcl_f,pcl_cld\random
   WriteLine pcl_f,pcl_cld\speed#
   WriteLine pcl_f,pcl_cld\renderstep
   For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
    If pcl_cld\pivot=pcl_gr\pivot
     WriteLine pcl_f,"group" 
     WriteLine pcl_f,pcl_gr\number
     WriteLine pcl_f,pcl_gr\near
     WriteLine pcl_f,pcl_gr\near_n#
     WriteLine pcl_f,pcl_gr\near_f#
     WriteLine pcl_f,pcl_gr\autofade
     WriteLine pcl_f,pcl_gr\far_n#
     WriteLine pcl_f,pcl_gr\far_f#
     WriteLine pcl_f,pcl_gr\size_min#
     WriteLine pcl_f,pcl_gr\size_max#
     WriteLine pcl_f,pcl_gr\size_mode
     WriteLine pcl_f,pcl_gr\pos_min#
     WriteLine pcl_f,pcl_gr\pos_max#
     WriteLine pcl_f,pcl_gr\rot_x
     WriteLine pcl_f,pcl_gr\rot_y
     WriteLine pcl_f,pcl_gr\rot_z
     WriteLine pcl_f,pcl_gr\rand_x
     WriteLine pcl_f,pcl_gr\rand_y
     WriteLine pcl_f,pcl_gr\rand_z
     WriteLine pcl_f,pcl_gr\alpha_min#
     WriteLine pcl_f,pcl_gr\alpha_max#
     WriteLine pcl_f,pcl_gr\shini_min#
     WriteLine pcl_f,pcl_gr\shini_max#
     WriteLine pcl_f,pcl_gr\r1
     WriteLine pcl_f,pcl_gr\g1
     WriteLine pcl_f,pcl_gr\b1
     WriteLine pcl_f,pcl_gr\r2
     WriteLine pcl_f,pcl_gr\g2
     WriteLine pcl_f,pcl_gr\b2
     WriteLine pcl_f,pcl_gr\blend
     WriteLine pcl_f,pcl_gr\fx
     WriteLine pcl_f,pcl_gr\p_type
     WriteLine pcl_f,pcl_gr\pick
     WriteLine pcl_f,pcl_gr\obscure
     For a=0 To 7
      WriteLine pcl_f,pcl_gr\texture$[a]
      WriteLine pcl_f,pcl_gr\texflag[a]
      WriteLine pcl_f,pcl_gr\texmode[a]	
      WriteLine pcl_f,pcl_gr\texscx#[a]	
      WriteLine pcl_f,pcl_gr\texscy#[a]	
      WriteLine pcl_f,pcl_gr\texposx#[a]	
      WriteLine pcl_f,pcl_gr\texposy#[a]	
      WriteLine pcl_f,pcl_gr\texrot#[a]	
     Next
    EndIf
   Next
   CloseFile(pcl_f)
  EndIf
  Exit
 EndIf
Next
End Function

Function PCL_LoadCloud(pcl_load$,parent=0)
pcl_f=ReadFile(pcl_load$)
If pcl_f<>0
 pcl_newcloud.pcl_cloud=New pcl_cloud
 pcl_newcloud\pivot=CreatePivot()
 pcl_newcloud\anz_part=ReadLine (pcl_f)
 pcl_newcloud\akt_part=1
 pcl_newcloud\groups=ReadLine (pcl_f)
 For a=1 To pcl_newcloud\anz_part
  pcl_newcloud\partx#[a]=ReadLine (pcl_f)
  pcl_newcloud\party#[a]=ReadLine (pcl_f)
  pcl_newcloud\partz#[a]=ReadLine (pcl_f)
  pcl_rx=ReadLine (pcl_f) : pcl_ry=ReadLine (pcl_f) : pcl_rz=ReadLine (pcl_f)
  pcl_newcloud\part11[a]=ReadLine (pcl_f)
  pcl_newcloud\part12[a]=ReadLine (pcl_f)
  pcl_newcloud\part10#[a]=ReadLine (pcl_f)
  pcl_newcloud\part1[a]=pcl_createflat(pcl_newcloud\pivot)
  PositionEntity pcl_newcloud\part1[a],pcl_newcloud\partx#[a],pcl_newcloud\party#[a],pcl_newcloud\partz#[a]
  RotateEntity pcl_newcloud\part1[a],pcl_rx,pcl_ry,pcl_rz
  pcl_newcloud\part6#[a]=EntityDistance#(pcl_newcloud\part1[a],pcl_newcloud\pivot)
  pcl_newcloud\part7#[a]=1 : pcl_newcloud\part8#[a]=1
 Next
 pcl_newcloud\r_amb#=ReadLine (pcl_f)
 pcl_newcloud\g_amb#=ReadLine (pcl_f)
 pcl_newcloud\b_amb#=ReadLine (pcl_f)
 ;pcl_newcloud\autofade=ReadLine (pcl_f)
 ;pcl_newcloud\fade_min#=ReadLine (pcl_f)
 ;pcl_newcloud\fade_max#=ReadLine (pcl_f)
 ;PCL_SetAutofade(pcl_newcloud\pivot,pcl_newcloud\fade_min#,pcl_newcloud\fade_max#)
 pcl_newcloud\auto=ReadLine (pcl_f)
 pcl_newcloud\point=ReadLine (pcl_f)
 pcl_newcloud\random=ReadLine (pcl_f)
 pcl_newcloud\speed#=ReadLine (pcl_f)
 pcl_newcloud\renderstep=ReadLine (pcl_f)
 Repeat 
  b$=ReadLine$ (pcl_f)
  If b$="group" 
   pcl_gr.pcl_groupbrush=New pcl_groupbrush
   pcl_gr\pivot=pcl_newcloud\pivot
   pcl_gr\number=ReadLine (pcl_f)
   pcl_gr\near=ReadLine (pcl_f)
   pcl_gr\near_n#=ReadLine (pcl_f)
   pcl_gr\near_f#=ReadLine (pcl_f)
   If pcl_gr\near>0 Then pcl_setnearfade(pcl_gr\pivot,pcl_gr\near,pcl_gr\near_n#,pcl_gr\near_f#,pcl_gr\number)
   pcl_gr\autofade=ReadLine (pcl_f)
   pcl_gr\far_n#=ReadLine (pcl_f)
   pcl_gr\far_f#=ReadLine (pcl_f)
   If pcl_gr\autofade=1 Then pcl_setautofade(pcl_gr\pivot,pcl_gr\far_n#,pcl_gr\far_f#,pcl_gr\number)
   pcl_gr\size_min#=ReadLine (pcl_f)
   pcl_gr\size_max#=ReadLine (pcl_f)
   pcl_gr\size_mode=ReadLine (pcl_f)
   pcl_setparticlescale(pcl_gr\pivot,pcl_gr\size_min#,pcl_gr\size_max#,pcl_gr\size_mode,pcl_gr\number)
   pcl_gr\pos_min#=ReadLine (pcl_f)
   pcl_gr\pos_max#=ReadLine (pcl_f)
   PCL_SetRandomPos(pcl_gr\pivot,pcl_gr\pos_min#,pcl_gr\pos_max#,pcl_gr\number)
   pcl_gr\rot_x=ReadLine (pcl_f)
   pcl_gr\rot_y=ReadLine (pcl_f)
   pcl_gr\rot_z=ReadLine (pcl_f)
   pcl_gr\rand_x=ReadLine (pcl_f)
   pcl_gr\rand_y=ReadLine (pcl_f)
   pcl_gr\rand_z=ReadLine (pcl_f)
   If pcl_newcloud\point=0 Then PCL_RotateParticle(pcl_gr\pivot,pcl_gr\rot_x,pcl_gr\rot_y,pcl_gr\rot_z,pcl_gr\rand_x,pcl_gr\rand_y,pcl_gr\rand_z,pcl_gr\number)
   pcl_gr\alpha_min#=ReadLine (pcl_f)
   pcl_gr\alpha_max#=ReadLine (pcl_f)
   PCL_SetCloudAlpha(pcl_gr\pivot,pcl_gr\alpha_min#,pcl_gr\alpha_max#,pcl_gr\number)
   pcl_gr\shini_min#=ReadLine (pcl_f)
   pcl_gr\shini_max#=ReadLine (pcl_f)
   PCL_SetCloudShininess(pcl_gr\pivot,pcl_gr\shini_min#,pcl_gr\shini_max#,pcl_gr\number)
   pcl_gr\r1=ReadLine (pcl_f)
   pcl_gr\g1=ReadLine (pcl_f)
   pcl_gr\b1=ReadLine (pcl_f)
   pcl_gr\r2=ReadLine (pcl_f)
   pcl_gr\g2=ReadLine (pcl_f)
   pcl_gr\b2=ReadLine (pcl_f)
   PCL_SetParticleColor(pcl_gr\pivot,pcl_gr\r1,pcl_gr\g1,pcl_gr\b1,pcl_gr\r2,pcl_gr\g2,pcl_gr\b2,pcl_gr\number)
   pcl_gr\blend=ReadLine (pcl_f)
   PCL_SetCloudBlend(pcl_gr\pivot,pcl_gr\blend,pcl_gr\number)
   pcl_gr\fx=ReadLine (pcl_f)
   PCL_SetCloudFx(pcl_gr\pivot,pcl_gr\fx,pcl_gr\number)
   pcl_gr\p_type=ReadLine (pcl_f)
   pcl_gr\pick=ReadLine (pcl_f)
   pcl_gr\obscure=ReadLine (pcl_f)
   PCL_SetParticleType(pcl_gr\pivot,pcl_gr\p_type,pcl_gr\pick,pcl_gr\obscure,pcl_gr\number)
   For a=0 To 7
    pcl_gr\texture$[a]=ReadLine (pcl_f)
    pcl_gr\texflag[a]=ReadLine (pcl_f)
    pcl_gr\texmode[a]=ReadLine (pcl_f)
    pcl_gr\texscx#[a]=ReadLine (pcl_f)	
    pcl_gr\texscy#[a]=ReadLine (pcl_f)	
    pcl_gr\texposx#[a]=ReadLine (pcl_f)	
    pcl_gr\texposy#[a]=ReadLine (pcl_f)	
    pcl_gr\texrot#[a]=ReadLine (pcl_f)	
	  If pcl_gr\texture$[a]<>"" Then PCL_SetCloudTexture(pcl_gr\pivot,pcl_gr\texture$[a],pcl_gr\texflag[a],pcl_gr\texmode[a],pcl_gr\texscx#[a],pcl_gr\texscy#[a],pcl_gr\texrot#[a],pcl_gr\texposx#[a],pcl_gr\texposy#[a],a,pcl_gr\number)
   Next
  EndIf
 Until b$=""
 CloseFile(pcl_f)
EndIf
Return pcl_newcloud\pivot
End Function

Function PCL_CopyCloud(pcl_pivot)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  pcl_newcloud.pcl_cloud=New pcl_cloud
  pcl_newcloud\pivot=CreatePivot()
  For a=1 To pcl_cld\anz_part
   pcl_newcloud\part1[a]=CopyEntity(pcl_cld\part1[a])
   pcl_newcloud\part2[a]=pcl_cld\part2[a]
   pcl_newcloud\part3[a]=pcl_cld\part3[a]
   pcl_newcloud\part4[a]=pcl_cld\part4[a]
   pcl_newcloud\part5#[a]=pcl_cld\part5#[a]
   pcl_newcloud\part6#[a]=pcl_cld\part6#[a]
   pcl_newcloud\part7#[a]=pcl_cld\part7#[a]
   pcl_newcloud\part8#[a]=pcl_cld\part8#[a]
   pcl_newcloud\part9#[a]=pcl_cld\part9#[a]
   pcl_newcloud\part10#[a]=pcl_cld\part10#[a]
   pcl_newcloud\part11[a]=pcl_cld\part11[a]
   pcl_newcloud\part12[a]=pcl_cld\part12[a]
   pcl_newcloud\part_n#[a]=pcl_cld\part_n#[a]
   pcl_newcloud\part_f#[a]=pcl_cld\part_f#[a]
   pcl_newcloud\partx#[a]=pcl_cld\partx#[a]
   pcl_newcloud\party#[a]=pcl_cld\party#[a]
   pcl_newcloud\partz#[a]=pcl_cld\partz#[a]
   EntityParent pcl_newcloud\part1[a],pcl_newcloud\pivot
   PositionEntity pcl_newcloud\part1[a],EntityX#(pcl_cld\part1[a]),EntityY#(pcl_cld\part1[a]),EntityZ#(pcl_cld\part1[a])
   RotateEntity pcl_newcloud\part1[a],EntityPitch#(pcl_cld\part1[a]),EntityYaw#(pcl_cld\part1[a]),EntityRoll#(pcl_cld\part1[a])
  Next
  pcl_newcloud\light=pcl_cld\light
  pcl_newcloud\camera=pcl_cld\camera
  pcl_newcloud\r_amb#=pcl_cld\r_amb#
  pcl_newcloud\g_amb#=pcl_cld\g_amb#
  pcl_newcloud\b_amb#=pcl_cld\b_amb#
  pcl_newcloud\auto=pcl_cld\auto
  pcl_newcloud\akt_part=pcl_cld\akt_part
  pcl_newcloud\anz_part=pcl_cld\anz_part
  pcl_newcloud\point=pcl_cld\point
  pcl_newcloud\random=pcl_cld\random
  pcl_newcloud\speed#=pcl_cld\speed#
  pcl_newcloud\renderstep=pcl_cld\renderstep
  pcl_newcloud\groups=pcl_cld\groups
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot
    temp.pcl_groupbrush=New pcl_groupbrush
    temp\pivot=pcl_newcloud\pivot
    temp\number=pcl_gr\number
    temp\near=pcl_gr\near
    temp\near_n#=pcl_gr\near_n#
    temp\near_f#=pcl_gr\near_f#
    temp\autofade=pcl_gr\autofade
    temp\far_n#=pcl_gr\far_n#
    temp\far_f#=pcl_gr\far_f#
    temp\size_min#=pcl_gr\size_min#
    temp\size_max#=pcl_gr\size_max#
    temp\size_mode=pcl_gr\size_mode
    temp\pos_min#=pcl_gr\pos_min#
    temp\pos_max#=pcl_gr\pos_max#
    temp\rot_x=pcl_gr\rot_x
    temp\rot_y=pcl_gr\rot_y
    temp\rot_z=pcl_gr\rot_z
    temp\rand_x=pcl_gr\rand_x
    temp\rand_y=pcl_gr\rand_y
    temp\rand_z=pcl_gr\rand_z
    temp\alpha_min#=pcl_gr\alpha_min#
    temp\alpha_max#=pcl_gr\alpha_max#
    temp\shini_min#=pcl_gr\shini_min#
    temp\shini_max#=pcl_gr\shini_max#
    temp\r1=pcl_gr\r1 : temp\g1=pcl_gr\g1 : temp\b1=pcl_gr\b1
    temp\r2=pcl_gr\r2 : temp\g2=pcl_gr\g2 : temp\b2=pcl_gr\b2
    temp\blend=pcl_gr\blend
    temp\fx=pcl_gr\fx
    temp\p_type=pcl_gr\p_type
    temp\pick=pcl_gr\pick
    temp\obscure=pcl_gr\obscure
    For a=1 To 7
     temp\texture$[a]=pcl_gr\texture$[a]
     temp\texflag[a]=pcl_gr\texflag[a]
     temp\texmode[a]=pcl_gr\texmode[a]
    Next
   EndIf
  Next
  Exit
 EndIf
Next
PositionEntity pcl_newcloud\pivot,0,0,0
Return pcl_newcloud\pivot
End Function

Function PCL_DeleteCloud(pcl_pivot)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  FreeEntity pcl_cld\pivot
  Delete pcl_cld
  Exit
 EndIf
Next
End Function

Function PCL_SetTimer()
PCL_TIMER=MilliSecs()
End Function

Function PCL_UpdateClouds(pcl_looptime#)
If pcl_looptime#=0
 pcl_looptime#=(MilliSecs()-PCL_TIMER)/1000.0
 PCL_TIMER=MilliSecs()
EndIf
pcl_cld.pcl_cloud=First pcl_cloud
If pcl_cld<>null
 If pcl_cld\auto=1 And pcl_cld\light<>0 And pcl_cld\camera<>0
  For a=pcl_cld\akt_part To pcl_cld\akt_part+10
   If a>pcl_cld\anz_part Then a=1 : Exit
   If EntityInView(pcl_cld\part1[a],pcl_cld\camera)=1
    pcl_rx=EntityPitch#(pcl_cld\part1[a])
    pcl_ry=EntityYaw#(pcl_cld\part1[a])
    pcl_rz=EntityRoll#(pcl_cld\part1[a])
    PointEntity pcl_cld\part1[a],pcl_cld\light
    c=100
    For b=1 To pcl_cld\anz_part 
     If b<>a And EntityDistance(pcl_cld\part1[b],pcl_cld\light) < EntityDistance(pcl_cld\part1[a],pcl_cld\light)
      TFormPoint EntityX#(pcl_cld\part1[b],1),EntityY#(pcl_cld\part1[b],1),EntityZ#(pcl_cld\part1[b],1),0,pcl_cld\part1[a]
	    ztemp#=TFormedZ#() 
      If ztemp#>0
	     xtemp#=TFormedX#() : ytemp#=TFormedY#()
	     xtemp_a#=xtemp#*(pcl_cld\part5#[a]+pcl_cld\part5#[b])
	     ytemp_a#=ytemp#*(pcl_cld\part5#[a]+pcl_cld\part5#[b])
       If Abs(xtemp_a#)<pcl_cld\part5#[a] And Abs(ytemp_a#)<pcl_cld\part5#[a]
	      c=c-pcl_cld\renderstep*pcl_cld\part9#[b]
	      If c<0 Then c=0 : Exit
	     EndIf
	    EndIf
	   EndIf
    Next
    pcl_cld\part8#[a]=c/100.0
    RotateEntity pcl_cld\part1[a],pcl_rx,pcl_ry,pcl_rz
   EndIf
  Next
  pcl_cld\akt_part=a
 EndIf
 Insert pcl_cld.pcl_cloud After Last pcl_cloud	
endif
For pcl_cld.pcl_cloud=Each pcl_cloud
 pcl_piv_scale#=(PCL_EntityScale#(pcl_cld\pivot,0)+PCL_EntityScale#(pcl_cld\pivot,1)+PCL_EntityScale#(pcl_cld\pivot,2))/3.0
 For a=1 To pcl_cld\anz_part
  If pcl_cld\part8#[a]<>pcl_cld\part7#[a]
   If pcl_cld\part8#[a]<pcl_cld\part7#[a]
    pcl_cld\part7#[a]=pcl_cld\part7#[a]-pcl_cld\speed#*pcl_looptime#
    If pcl_cld\part7#[a]<pcl_cld\part8#[a] Then pcl_cld\part7#[a]=pcl_cld\part8#[a]
   EndIf
   If pcl_cld\part8#[a]>pcl_cld\part7#[a]
    pcl_cld\part7#[a]=pcl_cld\part7#[a]+pcl_cld\speed#*pcl_looptime#
    If pcl_cld\part7#[a]>pcl_cld\part8#[a] Then pcl_cld\part7#[a]=pcl_cld\part8#[a]
   EndIf
   r=pcl_cld\part2[a]*((1.0-pcl_cld\r_amb#)*pcl_cld\part7#[a]+pcl_cld\r_amb#)
   g=pcl_cld\part3[a]*((1.0-pcl_cld\g_amb#)*pcl_cld\part7#[a]+pcl_cld\g_amb#)
   b=pcl_cld\part4[a]*((1.0-pcl_cld\b_amb#)*pcl_cld\part7#[a]+pcl_cld\b_amb#)
   EntityColor pcl_cld\part1[a],r,g,b
  EndIf
  If pcl_cld\camera<>0
   pcl_dist#=EntityDistance#(pcl_cld\part1[a],pcl_cld\camera)
   pcl_alpha#=1
   Select pcl_cld\part12[a]
    Case 1
     pcl_border#=pcl_piv_scale*(pcl_cld\part5#[a]/1.5)
     If pcl_dist#<pcl_border#
      pcl_alpha#=(((pcl_dist#-(pcl_border#/2))*2)/pcl_border#)*pcl_cld\part9#[a]
      EntityAlpha pcl_cld\part1[a],pcl_alpha#
     Else
      EntityAlpha pcl_cld\part1[a],pcl_cld\part9#[a]
      pcl_alpha#=1
     EndIf
    Case 2
     If pcl_dist#>pcl_cld\part_n#[a] And pcl_dist#<pcl_cld\part_f#[a]
      pcl_alpha#=(pcl_dist#-pcl_cld\part_n#[a])/(pcl_cld\part_f#[a]-pcl_cld\part_n#[a])*pcl_cld\part9#[a]
      EntityAlpha pcl_cld\part1[a],pcl_alpha#
     Else
      If pcl_dist#<pcl_cld\part_n#[a] And pcl_alpha#>0 Then EntityAlpha pcl_cld\part1[a],0 : pcl_alpha#=0
      If pcl_dist#>pcl_cld\part_f#[a] And pcl_alpha#<1 Then EntityAlpha pcl_cld\part1[a],pcl_cld\part9#[a] : pcl_alpha#=1
     EndIf
   End Select
   If pcl_cld\point=1 And pcl_alpha#>0 Then PointEntity pcl_cld\part1[a],pcl_cld\camera,(360*pcl_cld\part10#[a])*pcl_cld\random
  EndIf
 Next
Next
End Function

Function PCL_UpdateCloud(pcl_pivot)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  If pcl_cld\light<>0
   For a=1 To pcl_cld\anz_part
    pcl_rx=EntityPitch#(pcl_cld\part1[a])
    pcl_ry=EntityYaw#(pcl_cld\part1[a])
    pcl_rz=EntityRoll#(pcl_cld\part1[a])
    PointEntity pcl_cld\part1[a],pcl_cld\light
    c=100
    For b=1 To pcl_cld\anz_part
	 If b<>a And EntityDistance(pcl_cld\part1[b],pcl_cld\light) < EntityDistance(pcl_cld\part1[a],pcl_cld\light)
      TFormPoint EntityX#(pcl_cld\part1[b],1),EntityY#(pcl_cld\part1[b],1),EntityZ#(pcl_cld\part1[b],1),0,pcl_cld\part1[a]
	  ztemp#=TFormedZ#() 
      If ztemp#>0
	   xtemp#=TFormedX#() : ytemp#=TFormedY#()
	   xtemp_a#=xtemp#*(pcl_cld\part5#[a]+pcl_cld\part5#[b])
	   ytemp_a#=ytemp#*(pcl_cld\part5#[a]+pcl_cld\part5#[b])
       If Abs(xtemp_a#)<pcl_cld\part5#[a] And Abs(ytemp_a#)<pcl_cld\part5#[a]
	    c=c-pcl_cld\renderstep*pcl_cld\part9#[b]
	    If c<0 Then c=0 : Exit
	   EndIf
	  EndIf
	 EndIf
    Next
    pcl_cld\part8#[a]=c/100.0
    RotateEntity pcl_cld\part1[a],pcl_rx,pcl_ry,pcl_rz
   Next
  EndIf
  For a=1 To pcl_cld\anz_part
   If pcl_cld\part8#[a]<>pcl_cld\part7#[a]
    pcl_cld\part7#[a]=pcl_cld\part8#[a]
    r=pcl_cld\part2[a]*((1.0-pcl_cld\r_amb#)*pcl_cld\part7#[a]+pcl_cld\r_amb#)
    g=pcl_cld\part3[a]*((1.0-pcl_cld\g_amb#)*pcl_cld\part7#[a]+pcl_cld\g_amb#)
    b=pcl_cld\part4[a]*((1.0-pcl_cld\b_amb#)*pcl_cld\part7#[a]+pcl_cld\b_amb#)
    EntityColor pcl_cld\part1[a],r,g,b
   EndIf
  Next
  Exit
 EndIf
Next
End Function

Function PCL_SetLight(pcl_pivot,pcl_entity,pcl_renderstep)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
   pcl_cld\light=pcl_entity
   pcl_cld\renderstep=pcl_renderstep
  Exit
 EndIf
Next
End Function

Function PCL_SetAmbientLight(pcl_pivot,pcl_r,pcl_g,pcl_b)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  pcl_cld\r_amb#=pcl_r/255.0
  pcl_cld\g_amb#=pcl_g/255.0
  pcl_cld\b_amb#=pcl_b/255.0
  Exit
 EndIf
Next
End Function

Function PCL_SetAutoLightning(pcl_pivot,pcl_auto)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  pcl_cld\auto=pcl_auto
  If pcl_cld\auto=0
   For a=1 To pcl_cld\anz_part
    EntityColor pcl_cld\part1[a],pcl_cld\part2[a],pcl_cld\part3[a],pcl_cld\part4[a]
	pcl_cld\part7#[a]=1 : pcl_cld\part8#[a]=1
   Next
  EndIf   
  Exit
 EndIf
Next
End Function

Function PCL_SetCamera(pcl_pivot,pcl_cam,pcl_point,pcl_random=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  pcl_cld\camera=pcl_cam
  pcl_cld\point=pcl_point
  pcl_cld\random=pcl_random
  If pcl_cld\camera<>0 And pcl_cld\point=1
   For a=1 To pcl_cld\anz_part
    PointEntity pcl_cld\part1[a],pcl_cld\camera,(360*pcl_cld\part10#[a])*pcl_cld\random
   Next
  EndIf
  Exit
 EndIf
Next
End Function

Function PCL_PointParticle(pcl_pivot,pcl_entity,pcl_random=0,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot And pcl_entity>0
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a]
    PointEntity pcl_cld\part1[a],pcl_entity,(360*pcl_cld\part10#[a])*pcl_random
   EndIf
  Next
  Exit
 EndIf
Next
End Function


Function PCL_SetNearFade(pcl_pivot,pcl_nearfade,pcl_near#=0,pcl_far#=0,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\near=pcl_nearfade : pcl_gr\near_n#=pcl_near# : pcl_gr\near_f#=pcl_far#
   EndIf
  Next
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a] 
    pcl_cld\part12[a]=pcl_nearfade : pcl_cld\part_n#[a]=pcl_near# : pcl_cld\part_f#[a]=pcl_far#
    If pcl_nearfade=0 Then EntityAlpha pcl_cld\part1[a],pcl_cld\part9#[a]
   EndIf
  Next
  Exit
 EndIf
Next
End Function

Function PCL_SetAutofade(pcl_pivot,pcl_near#,pcl_far#,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    If pcl_near#<0 And pcl_far#<0
     pcl_gr\far_n#=-1 : pcl_gr\far_f#=-1 : pcl_gr\autofade=0 : pcl_autofade=0
    Else
     pcl_gr\far_n#=pcl_near# : pcl_gr\far_f#=pcl_far# : pcl_gr\autofade=1 : pcl_autofade=1
    EndIf
   EndIf
  Next

  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a]
    If pcl_autofade=1
     EntityAutoFade pcl_cld\part1[a],pcl_near#,pcl_far#
    Else
     b=pcl_createflat(pcl_cld\pivot)
     PositionEntity b,EntityX#(pcl_cld\part1[a]),EntityY#(pcl_cld\part1[a]),EntityZ#(pcl_cld\part1[a])
     RotateEntity b,EntityPitch#(pcl_cld\part1[a]),EntityYaw#(pcl_cld\part1[a]),EntityRoll#(pcl_cld\part1[a])
     PaintEntity b,GetEntityBrush(pcl_cld\part1[a])
     ScaleEntity b,pcl_cld\part5#[a],pcl_cld\part5#[a],pcl_cld\part5#[a]
     FreeEntity pcl_cld\part1[a] : pcl_cld\part1[a]=b
     For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
      If pcl_cld\pivot=pcl_gr\pivot And pcl_group=pcl_gr\number
       EntityPickMode pcl_cld\part1[a],pcl_gr\pick,pcl_gr\obscure
       EntityType pcl_cld\part1[a],pcl_gr\p_type
      EndIf
      Exit
     Next
    EndIf
   EndIf
  Next
  Exit
 EndIf
Next
End Function

Function PCL_RotateParticle(pcl_pivot,pcl_x,pcl_y,pcl_z,pcl_rx,pcl_ry,pcl_rz,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\rot_x=pcl_x : pcl_gr\rot_y=pcl_y : pcl_gr\rot_z=pcl_z : pcl_gr\rand_x=pcl_rx : pcl_gr\rand_y=pcl_ry : pcl_gr\rand_z=pcl_rz
   EndIf
  Next
  If pcl_cld\point=0
   For a=1 To pcl_cld\anz_part
    If pcl_group=0 Or pcl_group=pcl_cld\part11[a]
     RotateEntity pcl_cld\part1[a],pcl_x+(pcl_cld\part10#[a]*pcl_rx-pcl_rx/2),pcl_y+(pcl_cld\part10#[a]*pcl_ry-pcl_ry/2),pcl_z+(pcl_cld\part10#[a]*pcl_rz-pcl_rz/2)
    EndIf
   Next
  EndIf
  Exit
 EndIf
Next
End Function

Function PCL_SetRandomPos(pcl_pivot,pcl_min#,pcl_max#,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\pos_min#=pcl_min# : pcl_gr\pos_max#=pcl_max#
   EndIf
  Next
  pcl_sx#=PCL_EntityScale#(pcl_cld\pivot,0)
  pcl_sy#=PCL_EntityScale#(pcl_cld\pivot,1)
  pcl_sz#=PCL_EntityScale#(pcl_cld\pivot,2)
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a]
    pcl_rx=EntityPitch#(pcl_cld\part1[a])
    pcl_ry=EntityYaw#(pcl_cld\part1[a])
    pcl_rz=EntityRoll#(pcl_cld\part1[a])
    PositionEntity pcl_cld\part1[a],pcl_cld\partx#[a],pcl_cld\party#[a],pcl_cld\partz#[a]
    PointEntity pcl_cld\part1[a],pcl_cld\pivot
    PositionEntity pcl_cld\part1[a],0,0,0
    TurnEntity pcl_cld\part1[a],0,180,0
    If pcl_min#<pcl_max#
     MoveEntity pcl_cld\part1[a],0,0,pcl_cld\part6#[a]*((pcl_max#-pcl_min#)*pcl_cld\part10#[a]+pcl_min#)
    Else
     MoveEntity pcl_cld\part1[a],0,0,pcl_cld\part6#[a]*((pcl_min#-pcl_max#)*pcl_cld\part10#[a]+pcl_max#)
    EndIf
    RotateEntity pcl_cld\part1[a],pcl_rx,pcl_ry,pcl_rz
   EndIf
  Next
  Exit
 EndIf
Next
End Function

Function PCL_SetParticleScale(pcl_pivot,pcl_min#,pcl_max#,pcl_mode,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\size_min#=pcl_min# : pcl_gr\size_max#=pcl_max# : pcl_gr\size_mode=pcl_mode
   EndIf
  Next
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a]
    If pcl_min#<pcl_max#
     pcl_cld\part5#[a]=(pcl_max#-pcl_min#)*pcl_cld\part10#[a]+pcl_min#
    Else
     pcl_cld\part5#[a]=(pcl_min#-pcl_max#)*pcl_cld\part10#[a]+pcl_max#
    EndIf
    ScaleEntity pcl_cld\part1[a],pcl_cld\part5#[a],pcl_cld\part5#[a],pcl_cld\part5#[a]
    pcl_sx#=pcl_cld\part5#[a]*PCL_EntityScale#(pcl_cld\pivot,0)
    pcl_sy#=pcl_cld\part5#[a]*PCL_EntityScale#(pcl_cld\pivot,1)
    pcl_sz#=pcl_cld\part5#[a]*PCL_EntityScale#(pcl_cld\pivot,2)
    EntityBox pcl_cld\part1[a],pcl_sx#/(-2),pcl_sy#/(-2),pcl_sz#/(-2),pcl_sx#,pcl_sy#,pcl_sz#
    EntityRadius pcl_cld\part1[a],(pcl_sx#/2+pcl_sy#/2+pcl_sz#/2)/3.0
    Select pcl_mode
     Case 2
      PositionEntity pcl_cld\part1[a],pcl_cld\partx#[a],pcl_cld\party#[a],pcl_cld\partz#[a]
      pcl_rx#=EntityPitch#(pcl_cld\part1[a])
      pcl_ry#=EntityYaw#(pcl_cld\part1[a])
      pcl_rz#=EntityRoll#(pcl_cld\part1[a])
      PointEntity pcl_cld\part1[a],pcl_cld\pivot
      MoveEntity pcl_cld\part1[a],0,0,pcl_cld\part5#[a]/2
      RotateEntity pcl_cld\part1[a],pcl_rx,pcl_ry,pcl_rz
     Case 3
      PositionEntity pcl_cld\part1[a],pcl_cld\partx#[a],pcl_cld\party#[a],pcl_cld\partz#[a]
      pcl_rx#=EntityPitch#(pcl_cld\part1[a])
      pcl_ry#=EntityYaw#(pcl_cld\part1[a])
      pcl_rz#=EntityRoll#(pcl_cld\part1[a])
      PointEntity pcl_cld\part1[a],pcl_cld\pivot
      MoveEntity pcl_cld\part1[a],0,0,-pcl_cld\part5#[a]/2
      RotateEntity pcl_cld\part1[a],pcl_rx,pcl_ry,pcl_rz
    End Select
   EndIf
  Next
  Exit
 EndIf
Next
End Function

Function PCL_UpdateCollisionzones(pcl_pivot)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  pcl_px#=PCL_EntityScale#(pcl_cld\pivot,0)
  pcl_py#=PCL_EntityScale#(pcl_cld\pivot,1)
  pcl_pz#=PCL_EntityScale#(pcl_cld\pivot,2)
  For a=1 To pcl_cld\anz_part   
   pcl_sx#=pcl_cld\part5#[a]*pcl_px#
   pcl_sy#=pcl_cld\part5#[a]*pcl_py#
   pcl_sz#=pcl_cld\part5#[a]*pcl_pz#
   EntityBox pcl_cld\part1[a],pcl_sx#/(-2),pcl_sy#/(-2),pcl_sz#/(-2),pcl_sx#,pcl_sy#,pcl_sz#
   EntityRadius pcl_cld\part1[a],(pcl_sx#/2+pcl_sy#/2+pcl_sz#/2)/3.0
  Next
  Exit
 EndIf
Next
End Function

Function PCL_SetParticleColor(pcl_pivot,pcl_r1,pcl_g1,pcl_b1,pcl_r2,pcl_g2,pcl_b2,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\r1=pcl_r1 : pcl_gr\g1=pcl_g1 : pcl_gr\b1=pcl_b1
    pcl_gr\r2=pcl_r2 : pcl_gr\g2=pcl_g2 : pcl_gr\b2=pcl_b2
   EndIf
  Next
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a]
    pcl_f#=pcl_cld\part10#[a]
    If pcl_r1<pcl_r2 Then pcl_r=(pcl_r2-pcl_r1)*pcl_f#+pcl_r1 Else pcl_r=(pcl_r1-pcl_r2)*pcl_f#+pcl_r2
    If pcl_g1<pcl_g2 Then pcl_g=(pcl_g2-pcl_g1)*pcl_f#+pcl_g1 Else pcl_g=(pcl_g1-pcl_g2)*pcl_f#+pcl_g2
    If pcl_b1<pcl_b2 Then pcl_b=(pcl_b2-pcl_b1)*pcl_f#+pcl_b1 Else pcl_b=(pcl_b1-pcl_b2)*pcl_f#+pcl_b2
    pcl_cld\part2[a]=pcl_r : pcl_cld\part3[a]=pcl_g : pcl_cld\part4[a]=pcl_b
    r=pcl_cld\part2[a]*((1.0-pcl_cld\r_amb#)*pcl_cld\part7#[a]+pcl_cld\r_amb#)
    g=pcl_cld\part3[a]*((1.0-pcl_cld\g_amb#)*pcl_cld\part7#[a]+pcl_cld\g_amb#)
    b=pcl_cld\part4[a]*((1.0-pcl_cld\b_amb#)*pcl_cld\part7#[a]+pcl_cld\b_amb#)
    EntityColor pcl_cld\part1[a],r,g,b
;EntityColor pcl_cld\part1[a],255,255,255
   EndIf
  Next
  Exit
 EndIf
Next
End Function

Function PCL_SetColorSpeed(pcl_pivot,pcl_speed#)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  pcl_cld\speed#=pcl_speed#
  Exit
 EndIf
Next
End Function

Function PCL_SetCloudAlpha(pcl_pivot,pcl_min#,pcl_max#,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\alpha_min#=pcl_min# : pcl_gr\alpha_max#=pcl_max#
   EndIf
  Next
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a]
    If pcl_min#<pcl_max#
     pcl_cld\part9#[a]=(pcl_max#-pcl_min#)*pcl_cld\part10#[a]+pcl_min#
    Else
     pcl_cld\part9#[a]=(pcl_min#-pcl_max#)*pcl_cld\part10#[a]+pcl_max#
    EndIf
    EntityAlpha pcl_cld\part1[a],pcl_cld\part9#[a]
   EndIf
  Next
  Exit
 EndIf
Next
End Function

Function PCL_SetCloudShininess(pcl_pivot,pcl_min#,pcl_max#,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\shini_min#=pcl_min# : pcl_gr\shini_max#=pcl_max#
   EndIf
  Next
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a]
    If pcl_min#<pcl_max#
     b#=(pcl_max#-pcl_min#)*pcl_cld\part10#[a]+pcl_min#
    Else
     b#=(pcl_min#-pcl_max#)*pcl_cld\part10#[a]+pcl_max#
    EndIf
    EntityShininess pcl_cld\part1[a],b#
   EndIf
  Next
  Exit
 EndIf
Next
End Function

Function PCL_SetCloudTexture(pcl_pivot,pcl_texture$,pcl_flag=1,pcl_blend=2,pcl_scalex#=1,pcl_scaley#=1,pcl_rot#=0,pcl_x#=0,pcl_y#=0,pcl_layer=0,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  pcl_tex=LoadTexture(pcl_texture$,pcl_flag)
  If pcl_tex<>0
	 TextureBlend pcl_tex,pcl_blend : ScaleTexture pcl_tex,pcl_scalex#,pcl_scaley# : RotateTexture pcl_tex,pcl_rot#
	 PositionTexture pcl_tex,pcl_x#,pcl_y#
	endif
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number) And pcl_tex<>0
    b$=pcl_flipstring$(pcl_texture$)
    a=Instr(b$,"\",1)
    If a>0 Then b$=pcl_flipstring$(Left$(b$,a-1)) Else b$=pcl_texture$
    pcl_gr\texture$[pcl_layer]=b$ : pcl_gr\texflag[pcl_layer]=pcl_flag : pcl_gr\texmode[pcl_layer]=pcl_blend
    pcl_gr\texscx#[pcl_layer]=pcl_scalex# : pcl_gr\texscy#[pcl_layer]=pcl_scaley# : pcl_gr\texposx#[pcl_layer]=pcl_x#
    pcl_gr\texposy#[pcl_layer]=pcl_y# : pcl_gr\texrot#[pcl_layer]=pcl_rot#
   EndIf
  Next
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a] And pcl_tex<>0 Then EntityTexture pcl_cld\part1[a],pcl_tex,0,pcl_layer
  Next
  If pcl_tex<>0 Then FreeTexture pcl_tex
  Exit
 EndIf
Next
End Function

Function PCL_SetCloudBlend(pcl_pivot,pcl_blend,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\blend=pcl_blend
   EndIf
  Next
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a] Then EntityBlend pcl_cld\part1[a],pcl_blend
  Next
  Exit
 EndIf
Next
End Function

Function PCL_SetCloudFX(pcl_pivot,pcl_flag,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\fx=pcl_flag
   EndIf
  Next
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a] Then EntityFX pcl_cld\part1[a],pcl_flag
  Next
  Exit
 EndIf
Next
End Function


Function PCL_SetParticleType(pcl_pivot,pcl_type,pcl_pickmode,pcl_obscure,pcl_group=0)
For pcl_cld.pcl_cloud=Each pcl_cloud
 If pcl_cld\pivot=pcl_pivot
  For pcl_gr.pcl_groupbrush=Each pcl_groupbrush
   If pcl_cld\pivot=pcl_gr\pivot And (pcl_group=0 Or pcl_group=pcl_gr\number)
    pcl_gr\p_type=pcl_type : pcl_gr\pick=pcl_pickmode
    pcl_gr\obscure=pcl_obscure
   EndIf
  Next
  For a=1 To pcl_cld\anz_part
   If pcl_group=0 Or pcl_group=pcl_cld\part11[a]
    EntityPickMode pcl_cld\part1[a],pcl_pickmode,pcl_obscure : EntityType pcl_cld\part1[a],pcl_type
   EndIf
  Next
  Exit
 EndIf
Next
End Function






Function PCL_EntityScale#(entity,axis)
   x#=GetMatElement#(entity,axis,0)
   y#=GetMatElement#(entity,axis,1)
   z#=GetMatElement#(entity,axis,2)
   Return Sqr(x*x+y*y+z*z)
End Function

Function PCL_Createflat(parent)
obj=CreateMesh(parent)
surf=CreateSurface(obj)
v1=AddVertex(surf,.5,0,.5) : v2=AddVertex(surf,-.5,0,.5) : v3=AddVertex(surf,-.5,0,-.5) : v4=AddVertex(surf,.5,0,-.5)
v5=AddVertex(surf,0,0,0)
VertexTexCoords surf,v1,0,1 : VertexTexCoords surf,v2,1,1 : VertexTexCoords surf,v3,1,0 : VertexTexCoords surf,v4,0,0
VertexTexCoords surf,v5,.5,.5
t1=AddTriangle(surf,v2,v1,v5) : t1=AddTriangle(surf,v3,v2,v5) : t1=AddTriangle(surf,v4,v3,v5) : t1=AddTriangle(surf,v1,v4,v5)
UpdateNormals obj
au=5 : av=2 : mi=5
VertexNormal surf,0,au,av,au : VertexNormal surf,1,-au,av,au : VertexNormal surf,2,-au,av,-au : VertexNormal surf,3,au,av,-au
VertexNormal surf,4,0,mi,0
RotateMesh obj,90,0,0
Return obj
End Function

Function pcl_flipstring$(f$)
l=Len(f$)
For a=l To 1 Step -1
 f2$=f2$+Mid$(f$,a,1)
Next
Return f2$
End Function