Include"pcl-cloud2_6.bb"
Dim test(40,10)
Graphics3D 800,600,0,2

cam=CreateCamera()
CameraRange cam,.1,100000
CameraClsColor cam,0,150,255
PositionEntity cam,0,0,-20
CameraFogMode cam,1
CameraFogRange cam,2000,10000
CameraFogColor cam,0,150,255
cam2=CreatePivot(cam)

ground=CreatePlane()
PositionEntity ground,0,-1000,0
RotateEntity ground,0,-90,0
tg=LoadTexture("sand1.jpg")
ScaleTexture tg,1024,1024
EntityTexture ground,tg

sky=CreatePlane()
PositionEntity sky,0,2000,0
RotateEntity sky,180,0,0
EntityBlend sky,3
EntityFX sky,9
EntityOrder sky,1
tg=LoadTexture("nachtwolken.jpg",2)
ScaleTexture tg,2048,2048
EntityTexture sky,tg

l=CreateLight()
sun=LoadSprite("particle.jpg",8,l)

ScaleSprite sun,100,100 : EntityBlend sun,3
EntityColor sun,255,255,0
PositionEntity l,3000,80,0

AmbientLight 70,105,140

c=LoadMesh("cloud.3ds")
FitMesh c,-.5,-.5,-.5,1,1,1,1
ScaleEntity c,30,60,30
HideEntity c
c2=CreateSphere()
ScaleEntity c2,15,15,15
HideEntity c2

PointEntity l,c

For a=1 To 30
 test(a,2)=CreatePivot()
 test(a,4)=CreatePivot(test(a,2))
 test(a,0)=pcl_newcloud(c,50,3)
 test(a,1)=pcl_newcloud(c2,1)
 test(a,3)=pcl_newcloud(c2,1)
 EntityParent test(a,0),test(a,2)
 EntityParent test(a,1),test(a,4)
 EntityParent test(a,3),test(a,4)
 PositionEntity test(a,3),0,0,-2
 pcl_setlight(test(a,0),l,5)
 pcl_setlight(test(a,1),l,5)
 pcl_setlight(test(a,3),l,5)
 pcl_setcamera(test(a,0),cam,1)
 pcl_setcamera(test(a,1),cam,1)
 pcl_setcamera(test(a,3),cam,1)
 pcl_setparticlescale(test(a,0),10,22,1,1)
 pcl_setparticlescale(test(a,0),10,22,1,2)
 pcl_setparticlescale(test(a,0),8,13,1,3)
 pcl_setparticlescale(test(a,1),47,47,1)
 pcl_setparticlescale(test(a,3),50,50,1)
 pcl_setrandompos(test(a,1),0,0)
 pcl_setrandompos(test(a,3),0,0)
 pcl_setparticlecolor(test(a,0),255,255,255,255,255,255)
 pcl_setparticlecolor(test(a,1),200,200,200,245,245,245)
 pcl_setparticlecolor(test(a,3),255,255,255,255,255,255)
 pcl_setcloudtexture(test(a,0),"cloud3.tga",1+2+8,2,1,1,0,0,0,0,1)
 pcl_setcloudtexture(test(a,0),"cloud3.tga",1+2+8,2,1,1,0,0,0,0,2)
 pcl_setcloudtexture(test(a,0),"cloud2.tga",1+2+8,2,1,1,0,0,0,0,3)
 b=Rand(1,3)
 Select b
  Case 1
   pcl_setcloudtexture(test(a,1),"wolke2.tga",1+2+8,2)
   pcl_setcloudtexture(test(a,3),"wolke2.tga",1+2+8,1)
  Case 2
   pcl_setcloudtexture(test(a,1),"wolke1.tga",1+2+8,2)
   pcl_setcloudtexture(test(a,3),"wolke1.tga",1+2+8,1)
  Case 3
   pcl_setcloudtexture(test(a,1),"wolke.tga",1+2+8,2)
   pcl_setcloudtexture(test(a,3),"wolke.tga",1+2+8,1)
 End Select
 pcl_setcloudblend(test(a,0),1)
 pcl_setcloudblend(test(a,1),1)
 pcl_setcloudblend(test(a,3),3)
 pcl_setnearfade(test(a,0),1)
 pcl_setnearfade(test(a,1),2,600,1250)
 pcl_setnearfade(test(a,3),2,650,1300)
 pcl_setautofade(test(a,0),600,1250)
 pcl_setautofade(test(a,1),1500,1850)
 pcl_setautofade(test(a,3),1450,1800)
 pcl_setcloudalpha(test(a,0),.5,.75,1)
 pcl_setcloudalpha(test(a,0),.5,.75,2)
 pcl_setcloudalpha(test(a,0),.25,.75,3)
 pcl_setambientlight(test(a,0),70,105,140)
 pcl_setambientlight(test(a,1),70,105,140)
 PositionEntity test(a,2),Rnd(-1600,1600),Rnd(-100,100),Rnd(-1600,1600)
 s#=Rnd(3,20)
 ScaleEntity test(a,0),s#,s#/1.5,s#
 ScaleEntity test(a,1),s#,s#/2,s#
 ScaleEntity test(a,3),s#,s#/2,s#
 pcl_updatecloud(test(a,0))
 ;pcl_updatecloud(test(a,1))

Next

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

 If KeyDown(200)=1 Then speed#=speed#+300*looptime#
 If KeyDown(208)=1 Then speed#=speed#-300*looptime#
 If KeyDown(203)=1 Then MoveEntity cam,-400*looptime#,0,0
 If KeyDown(205)=1 Then MoveEntity cam,400*looptime#,0,0
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
 PositionEntity l,EntityX#(cam)+3000,EntityY#(cam)+80,EntityZ#(cam)
 PositionEntity sky,EntityX#(cam),EntityY#(cam)+1000,EntityZ#(cam)
 If KeyHit(57)=1
  fade=1-fade
 EndIf
 PointEntity cam2,l
 For a=1 To 30
  If EntityDistance(cam,test(a,2))>2000
   EntityParent test(a,2),cam
   PositionEntity test(a,2),0,0,0 : RotateEntity test(a,2),Rand(-10,10),Rand(-45,45),0
   MoveEntity test(a,2),0,0,1995
   EntityParent test(a,2),0
   RotateEntity test(a,2),0,0,0
  Else
   PointEntity test(a,4),cam
   w1=DeltaYaw(cam2,test(a,2))
   w2=DeltaYaw(cam2,l)
   w3#=(1-Abs(Sin(w2/2)-Sin(w1/2)))*2-1
   w1=DeltaPitch(cam2,test(a,2))
   w2=DeltaPitch(cam2,l)
   w4#=((1-Abs(Sin(w2/2)-Sin(w1/2)))*2-1)*w3#
   pcl_setcloudalpha(test(a,3),w4#,w4#)
  EndIf
 Next
 time3=MilliSecs()
 If fade=1 Then pcl_updateclouds(looptime#)
 time3=MilliSecs()-time3
 UpdateWorld()
 RenderWorld()
 Color 0,0,0
 Text 10,20,"Use mouse for steering and Arrow-Keys to speed up/down and slide"
 Text 10,30,"Press Space to switch updating on/off"
 Text 10,10,"FPS:"+fps+"   Updating:"+fade+"    Time(msecs):"+time3
 Flip
Wend
ClearWorld()