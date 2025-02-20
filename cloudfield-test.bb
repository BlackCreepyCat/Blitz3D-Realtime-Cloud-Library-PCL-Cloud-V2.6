Include"pcl-cloud2_6.bb"
Dim test(40,5)
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

c=LoadMesh("cloud.3ds")
FitMesh c,-.5,-.5,-.5,1,1,1,1
ScaleEntity c,30,60,30
HideEntity c
c2=CreateSphere()
ScaleEntity c2,10,10,10
HideEntity c2

PointEntity l,c

For a=1 To 30
 test(a,0)=pcl_newcloud(c,50,3)
 test(a,1)=pcl_newcloud(c2,20)
 pcl_setlight(test(a,0),l,5)
 pcl_setlight(test(a,1),l,5)
 pcl_setcamera(test(a,0),cam,1)
 pcl_setcamera(test(a,1),cam,1,1)
 pcl_setparticlescale(test(a,0),10,22,1,1)
 pcl_setparticlescale(test(a,0),10,22,1,2)
 pcl_setparticlescale(test(a,0),8,13,1,3)
 pcl_setparticlescale(test(a,1),13,17,1)
 pcl_setparticlecolor(test(a,0),255,255,255,255,255,255)
 pcl_setparticlecolor(test(a,1),200,200,200,245,245,245)
 pcl_setcloudtexture(test(a,0),"cloud3.tga",1+2+8,2,1,1,0,0,0,0,1)
 pcl_setcloudtexture(test(a,0),"cloud3.tga",1+2+8,2,1,1,0,0,0,0,2)
 pcl_setcloudtexture(test(a,0),"cloud2.tga",1+2+8,2,1,1,0,0,0,0,3)
 pcl_setcloudtexture(test(a,1),"rauch-particle.tga",1+2+8,2)
 pcl_setcloudblend(test(a,0),1)
 pcl_setcloudblend(test(a,1),1)
 pcl_setnearfade(test(a,0),1)
 pcl_setnearfade(test(a,1),1)
 pcl_setautofade(test(a,0),650,850)
 pcl_setautofade(test(a,1),650,850)
 pcl_setcloudalpha(test(a,0),.5,.75,1)
 pcl_setcloudalpha(test(a,0),.5,.75,2)
 pcl_setcloudalpha(test(a,0),.25,.75,3)
 pcl_setambientlight(test(a,0),70,105,140)
 pcl_setambientlight(test(a,1),70,105,140)
 PositionEntity test(a,0),Rnd(-600,600),Rnd(-10,10),Rnd(-600,600)
 s#=Rnd(3,20)
 ScaleEntity test(a,0),s#,s#/1.5,s#
 ScaleEntity test(a,1),s#,s#/4,s#
 pcl_updatecloud(test(a,0))
 pcl_updatecloud(test(a,1))
 PositionEntity test(a,1),EntityX(test(a,0)),EntityY(test(a,0))-20*(s#/2),EntityZ(test(a,0))

Next

fade=1
time=MilliSecs()
MoveMouse 400,300

While KeyHit(1)=0

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
  For a=1 To 30
  If fade=1
   pcl_setautofade(test(a,0),650,850)
  Else
   pcl_setautofade(test(a,0),-1,-1)
  EndIf
  Next
 EndIf

 For a=1 To 30
  If EntityDistance(cam,test(a,0))>1100
   EntityParent test(a,0),cam
   PositionEntity test(a,0),0,0,0 : RotateEntity test(a,0),Rand(-10,10),Rand(-45,45),0
   MoveEntity test(a,0),0,0,1095
   EntityParent test(a,0),0
   RotateEntity test(a,0),0,0,0
   PositionEntity test(a,1),EntityX(test(a,0)),EntityY(test(a,0))-20*(PCL_EntityScale#(test(a,0),1)),EntityZ(test(a,0))
  EndIf
 Next
 pcl_updateclouds(looptime#)

 UpdateWorld()
 RenderWorld()
 Color 0,0,0
 Text 10,20,"Use mouse for steering and Arrow-Keys to speed up/down and slide"
 Text 10,30,"Press Space to switch autofading on/off"
 Text 10,10,"Autofading:"+fade
 Flip
Wend
ClearWorld()