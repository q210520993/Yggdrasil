package com.c1ok.bedwars

/**
 * 资源生成器
 */
interface Generator {

    fun start()
    fun addGenerator(resourceType: SpawnResourceType)
    fun resetGenerator(resourceType: SpawnResourceType)
    fun removeGenerator(resourceType: SpawnResourceType)
    fun removeGenerator(id: String)
    fun getGenerator(id: String): SpawnResourceType?
    fun close()

}