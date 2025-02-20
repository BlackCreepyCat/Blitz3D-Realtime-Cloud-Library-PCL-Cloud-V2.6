Include"pcl-cloud2_6.bb"
Graphics3D 800,600,0,2

cam=CreateCamera()
CameraRange cam,.1,100000
CameraClsColor cam,0,150,255
PositionEntity cam,0,0,-20
CameraFogMode cam,1
CameraFogRange cam,2000,10000
CameraFogColor cam,0,150,255

ground=CreatePlane()
PositionEntity ground,0,-1000,0
RotateEntity ground,0,-90,0
tg=LoadTexture("sand1.jpg")
ScaleTexture tg,1024,1024
EntityTexture ground,tg

sky=CreatePlane()
PositionEntity sky,0,1000,0
RotateEntity sky,180,0,0
EntityBlend sky,3 : 
EntityFX sky,9
tg=LoadTexture("nachtwolken.jpg",2)
ScaleTexture tg,1024,1024
EntityTexture sky,tg

l=CreateLight()
sun=LoadSprite("particle.jpg",8,l)
ScaleSprite sun,100,100 : EntityBlend sun,3
EntityColor sun,255,255,0
PositionEntity l,10000,0,0

AmbientLight 70,105,140

c2=CreateCylinder(12,0)
;c2=CreateCube()
ScaleEntity c2,20,10,20
;HideEntity c2
EntityFX c2,16
PointEntity l,c2

For a=1 To 1
 test=pcl_newcloud(c2,100,3)
 pcl_setlight(test,l,5)
 pcl_setcamera(test,cam,1,1)
 pcl_setparticlescale(test,6,10,1,1)
 pcl_setparticlescale(test,13,17,1,2)
 pcl_setparticlescale(test,23,27,1,3)
 pcl_setrandompos(test,0,.2,1)
 pcl_setrandompos(test,1,1,2)
 pcl_setrandompos(test,1.5,2,3)
 pcl_setparticlecolor(test,255,0,0,255,255,0,1)
 pcl_setparticlecolor(test,255,255,255,255,255,255,2)
 pcl_setparticlecolor(test,200,255,200,0,255,0,3)
 pcl_setcloudtexture(test,"rauch-particle.tga",1+2+8,2,1,1,0,0,0,0,1)
 pcl_setcloudtexture(test,"cloud2.tga",1+2+8,2,1,1,0,0,0,0,2)
 pcl_setcloudtexture(test,"rauch-particle.tga",1+2+8,2,1,1,0,0,0,0,3)
 pcl_setcloudblend(test,3,1)
 pcl_setcloudblend(test,1,2)
 pcl_setcloudblend(test,1,3)
 pcl_setnearfade(test,1,0,0,2)
 pcl_setautofade(test,650,850)
 pcl_setcloudalpha(test,.25,.75,1)
 pcl_setcloudalpha(test,.75,1,2)
 pcl_setcloudalpha(test,.5,.75,3)
 pcl_setambientlight(test,70,105,140)
 ;PositionEntity test,Rnd(-600,600),Rnd(-10,10),Rnd(-600,600)
 s#=3
 ;ScaleEntity test,s#,s#,s#
 pcl_updatecloud(test)
Next
pcl_savecloud(test,"savedcloud.txt")

fade=1
time=MilliSecs()
MoveMouse 400,300

While KeyHit(1)=0
frame=frame+1
If frame=10
 fps=frame/((MilliSecs()-time2)/1000.0)
 time2=MilliSecs()
 frame=0
EndIf

 looptime#=(MilliSecs()-time)/1000.0
 time=MilliSecs()

 If KeyDown(200)=1 Then speed#=speed#+30*looptime#
 If KeyDown(208)=1 Then speed#=speed#-30*looptime#
 If KeyDown(203)=1 Then MoveEntity cam,-40*looptime#,0,0
 If KeyDown(205)=1 Then MoveEntity cam,40*looptime#,0,0
 If speed#<0 Then speed#=0
 MoveEntity cam,0,0,speed#*looptime#
 x=MouseX()-400 : y=MouseY()-300
 xm=xm+x+xms#
 ym=ym+y+yms#
 If x<>0 Then xms#=x
 If y<>0 Then yms#=y
 xms#=xms#/1.5
 yms#=yms#/1.5
 If ym>90 Then ym=90
 If ym<-90 Then ym=-90
 MoveMouse 400,300
 RotateEntity cam,ym,-xm,0
 PositionEntity l,EntityX#(cam)+1000,EntityY#(cam),EntityZ#(cam)
 PositionEntity sky,EntityX#(cam),EntityY#(cam)+1000,EntityZ#(cam)
 If KeyHit(57)=1
  fade=1-fade
  For a=1 To 1
  If fade=1
   pcl_setautofade(test,650,850)
  Else
   pcl_setautofade(test,-1,-1)
  EndIf
  Next
 EndIf

 For a=1 To 1
  If EntityDistance(cam,test)>1000
   EntityParent test,cam
   PositionEntity test,0,0,0 : RotateEntity test,Rand(-10,10),Rand(-45,45),0
   MoveEntity test,0,0,995
   EntityParent test,0
   RotateEntity test,0,0,0
  EndIf
 Next
 pcl_updateclouds(looptime#)

 UpdateWorld()
 RenderWorld()
 Color 0,0,0
 Text 10,20,"Use mouse for steering and Arrow-Keys to speed up/down and slide"+alpha$
 Text 10,10,fps
 Flip
Wend
ClearWorld()