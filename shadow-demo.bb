Include"pcl-cloud2_6.bb"

Graphics3D 800,600,0,2

cam=CreateCamera()
CameraClsColor cam,0,150,255
CameraRange cam,1,10000
PositionEntity cam,0,10,-150

l=CreateLight()
sun=LoadSprite("particle.jpg",2+8,l)
ScaleSprite sun,100,100 : EntityBlend sun,3
EntityColor sun,255,255,0
AmbientLight 50,60,70
PositionEntity l,1000,0,0

c=CreateSphere()
ScaleEntity c,50,30,50
test=pcl_newcloud(c,50)
FreeEntity c

pcl_setlight(test,l,5)
pcl_setcamera(test,cam,1,1)
pcl_setparticlescale(test,70,110,1)
pcl_setparticlecolor(test,255,255,255,255,255,255)
pcl_setrandompos(test,.5,1.4)
pcl_setcloudtexture(test,"rauch-particle.jpg",1+2+8,2)
pcl_setcloudblend(test,0)
pcl_setnearfade(test,1)
pcl_setambientlight(test,50,60,70)
pcl_setautolightning(test,1)
ScaleEntity test,1,1,1
PointEntity cam,test
time=MilliSecs() : au=1 : sc#=1 : tx=0 : alpha$="normal"

While KeyHit(1)=0
 looptime#=(MilliSecs()-time)/1000.0
 time=MilliSecs()

 w#=w#+.25
 If w#=360 Then w#=0
 x=Sin(w#)*1000
 z=Cos(w#)*1000

 If KeyHit(28)=1
  tx=1-tx
  Select tx
   Case 0
    pcl_setcloudtexture(test,"rauch-particle.jpg",1+2+8,2) : alpha$="normal"
   Case 1
    pcl_setcloudtexture(test,"rauch-particle.tga",1+2+8,2) : alpha$="Alpha-Channel"
  End Select
 EndIf
 If KeyHit(57)=1 Then au=1-au : pcl_setautolightning(test,au)
 If KeyDown(200)=1 Then sc#=sc#+.01 : ScaleEntity test,1,sc#,1
 If KeyDown(208)=1 Then sc#=sc#-.01 : ScaleEntity test,1,sc#,1

 PositionEntity l,x,0,z
 TurnEntity test,0,5*looptime#,0
 PointEntity l,test

 pcl_updateclouds(looptime#)

 UpdateWorld()
 RenderWorld()

 Color 0,0,0
 Text 10,10,"Press Enter to change between normal Alpha-Texture and AlphaChannel-Texture:  "+alpha$
 Text 10,25,"Press Space to switch Autolightning on/off:  "+au
 Text 10,40,"Press up and down to stretch the cloud:  "+sc#

 Flip
Wend

pcl_deletecloud(test)
ClearWorld()