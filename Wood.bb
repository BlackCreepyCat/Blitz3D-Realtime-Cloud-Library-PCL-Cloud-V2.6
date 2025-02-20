Include"pcl-cloud2_6.bb"
Dim test(200,5)
Graphics3D 800,600,0,1
campiv=CreatePivot()
cam=CreateCamera(campiv)
CameraRange cam,.1,100000
CameraClsColor cam,0,150,255
CameraFogMode cam,1
CameraFogRange cam,2000,10000
CameraFogColor cam,0,150,255

ground=CreatePlane()
PositionEntity ground,0,-15,0
RotateEntity ground,0,-90,0
tg=LoadTexture("sand1.jpg")
ScaleTexture tg,32,32
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
PositionEntity l,10000,100,0


AmbientLight 70,105,140

c2=CreateSphere()
ScaleEntity c2,10,10,10
HideEntity c2

PointEntity l,c2

For a=1 To 100
 test(a,1)=CreateCone(8)
 EntityColor test(a,1),200,100,0
 test(a,0)=pcl_newcloud(c2,40)
 pcl_setlight(test(a,0),l,15)
 pcl_setcamera(test(a,0),cam,0)
 pcl_setparticlescale(test(a,0),5,8,1)
 pcl_rotateparticle(test(a,0),0,0,0,360,360,360)
 pcl_setparticlecolor(test(a,0),200,200,200,255,255,255)
 pcl_setcloudtexture(test(a,0),"leafs.png",1+4+8,2)
 pcl_setcloudblend(test(a,0),1)
 pcl_setcloudfx(test(a,0),16)
 pcl_setautofade(test(a,0),650,850)
 EntityAutoFade test(a,1),650,850
 pcl_setautolightning(test(a,0),0)
 pcl_setrandompos(test(a,0),.2,.7)
 pcl_setambientlight(test(a,0),70,105,140)
 s#=Rnd(3,4)
 ScaleEntity test(a,0),s#,s#,s#
 ScaleEntity test(a,1),s#,s#*5,s#
 PositionEntity test(a,0),Rnd(-600,600),25,Rnd(-600,600)
 PositionEntity test(a,1),EntityX(test(a,0)),0,EntityZ(test(a,0))
 pcl_updatecloud(test(a,0))
Next

time=MilliSecs()
MoveMouse 400,300

While KeyHit(1)=0

 looptime#=(MilliSecs()-time)/1000.0
 time=MilliSecs()

 If KeyDown(200)=1 Then speed#=speed#+30*looptime#
 If KeyDown(208)=1 Then speed#=speed#-30*looptime#
 If KeyDown(203)=1 Then MoveEntity campiv,-40*looptime#,0,0
 If KeyDown(205)=1 Then MoveEntity campiv,40*looptime#,0,0
 If speed#<0 Then speed#=0
 MoveEntity campiv,0,0,speed#*looptime#
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
 RotateEntity campiv,0,-xm,0
 RotateEntity cam,ym,0,0
 PositionEntity l,EntityX#(campiv)+1000,EntityY#(campiv)+100,EntityZ#(campiv)
 PositionEntity sky,EntityX#(campiv),EntityY#(campiv)+1000,EntityZ#(campiv)
 For a=1 To 100
  If EntityDistance(cam,test(a,0))>1000
   EntityParent test(a,0),campiv
   PositionEntity test(a,0),0,25,0 : RotateEntity test(a,0),0,Rand(-45,45),0
   MoveEntity test(a,0),0,0,995
   EntityParent test(a,0),0
   RotateEntity test(a,0),0,0,0
   PositionEntity test(a,1),EntityX(test(a,0)),0,EntityZ(test(a,0))
  EndIf
 Next
 pcl_updateclouds(looptime#)

 UpdateWorld()
 RenderWorld()
 Color 0,0,0
 Text 10,10,"Use mouse for steering and Arrow-Keys to speed up/down and slide"+alpha$

 Flip
Wend
ClearWorld()