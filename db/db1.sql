USE [rannlab]
GO
/****** Object:  Table [dbo].[REGISTRATION]    Script Date: 10/27/2014 6:04:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[REGISTRATION](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](max) NULL,
	[mobile] [varchar](max) NULL,
	[otp] [varchar](max) NULL,
	[flag] [int] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SERVER_CONTACT]    Script Date: 10/27/2014 6:04:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SERVER_CONTACT](
	[contactno] [varchar](max) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[USER_DETAILS]    Script Date: 10/27/2014 6:04:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[USER_DETAILS](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[otp] [varchar](max) NULL,
	[name] [varchar](max) NULL,
	[mobile] [varchar](max) NULL,
	[emailid] [varchar](max) NULL,
	[gender] [varchar](max) NULL,
	[age] [varchar](max) NULL,
	[bloodgroup] [varchar](max) NULL,
	[address] [varchar](max) NULL,
	[employed] [varchar](max) NULL,
	[profession] [varchar](max) NULL,
	[mobileno1] [varchar](max) NULL,
	[mobileno2] [varchar](max) NULL,
	[mobileno3] [varchar](max) NULL,
	[message1] [varchar](max) NULL,
	[message2] [varchar](max) NULL,
	[defaultnumber] [varchar](max) NULL,
	[defaultmessage] [varchar](max) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
ALTER TABLE [dbo].[REGISTRATION] ADD  CONSTRAINT [DF_REGISTRATION_flag]  DEFAULT ((0)) FOR [flag]
GO
